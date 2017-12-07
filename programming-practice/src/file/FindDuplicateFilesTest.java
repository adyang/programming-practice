package file;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;

public class FindDuplicateFilesTest {
	private FileSystem fs;

	@Before
	public void setUp() throws Exception {
		fs = Jimfs.newFileSystem(Configuration.unix());
	}

	@After
	public void tearDown() throws Exception {
		fs.close();
	}

	@Test
	public void noDuplicates() throws IOException {
		Path fileOne = fs.getPath("/fileOne.txt");
		Files.write(fileOne, "fileOneContent".getBytes(StandardCharsets.UTF_8));
		Path fileTwo = fs.getPath("/fileTwo.txt");
		Files.write(fileTwo, "fileTwoContent".getBytes(StandardCharsets.UTF_8));

		assertDuplicateFilePaths(new FilePaths[0]);
	}

	@Test
	public void oneDuplicate_rootLevel() throws IOException {
		Path fileOne = fs.getPath("/fileOne.txt");
		Files.write(fileOne, "fileOneContent".getBytes(StandardCharsets.UTF_8));
		Path fileOneCopy = fs.getPath("/fileOneCopy.txt");
		Files.copy(fileOne, fileOneCopy);
		makeModifiedTimeLater(fileOneCopy);

		assertDuplicateFilePaths(new FilePaths(fileOneCopy, fileOne));
	}

	@Test
	public void multipleDuplicates_multipleLevels() throws IOException {
		Path fileOne = fs.getPath("/fileOne.txt");
		Files.write(fileOne, "fileOneContent".getBytes(StandardCharsets.UTF_8));
		Path fileOneCopy = fs.getPath("/fileOneCopy.txt");
		Files.copy(fileOne, fileOneCopy);
		makeModifiedTimeLater(fileOneCopy);

		Path dirOne = fs.getPath("/dirOne");
		Path dirTwo = dirOne.resolve("dirTwo");
		Files.createDirectories(dirTwo);

		Path fileTwo = dirTwo.resolve("fileTwo.txt");
		Files.write(fileTwo, "fileTwoContent".getBytes(StandardCharsets.UTF_8));
		Path fileTwoCopy = dirOne.resolve("fileTwoCopy.txt");
		Files.copy(fileTwo, fileTwoCopy);
		makeModifiedTimeLater(fileTwoCopy);

		assertDuplicateFilePaths(new FilePaths(fileOneCopy, fileOne), new FilePaths(fileTwoCopy, fileTwo));
	}

	@Test
	public void largerFiles_multipleDuplicates_multipleLevels() throws IOException {
		Path fileOne = fs.getPath("/fileOne.txt");
		writeClasspathResource(fileOne, "/findduplicates/fileOne.txt");
		Path fileOneCopy = fs.getPath("/fileOneCopy.txt");
		Files.copy(fileOne, fileOneCopy);
		makeModifiedTimeLater(fileOneCopy);

		Path dirOne = fs.getPath("/dirOne");
		Path dirTwo = dirOne.resolve("dirTwo");
		Files.createDirectories(dirTwo);

		Path fileTwo = dirTwo.resolve("fileTwo.txt");
		writeClasspathResource(fileTwo, "/findduplicates/fileTwo.txt");
		Path fileTwoCopy = dirOne.resolve("fileTwoCopy.txt");
		Files.copy(fileTwo, fileTwoCopy);
		makeModifiedTimeLater(fileTwoCopy);

		assertDuplicateFilePaths(new FilePaths(fileOneCopy, fileOne), new FilePaths(fileTwoCopy, fileTwo));
	}

	private void writeClasspathResource(Path file, String resourcePath) throws IOException {
		try (InputStream is = getClass().getResourceAsStream(resourcePath);
				OutputStream os = Files.newOutputStream(file);) {
			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = is.read(buffer)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
		}
	}

	private void assertDuplicateFilePaths(FilePaths... expectedFilePaths) {
		assertEquals(Arrays.asList(expectedFilePaths), findDuplicateFiles(fs.getPath("/")));
	}

	private void makeModifiedTimeLater(Path file) throws IOException {
		Files.setLastModifiedTime(file, incrementModifiedTime(file));
	}

	private FileTime incrementModifiedTime(Path file) throws IOException {
		return FileTime.fromMillis(Files.getLastModifiedTime(file).toMillis() + 1);
	}

	private List<FilePaths> findDuplicateFiles(Path startDirectory) {
		Deque<Path> stack = new ArrayDeque<>();
		stack.push(startDirectory);
		List<FilePaths> duplicates = new ArrayList<>();
		Map<String, FileInfo> seenFiles = new HashMap<>();
		while (!stack.isEmpty()) {
			Path currPath = stack.pop();
			if (Files.isDirectory(currPath)) {
				addContentsToStack(currPath, stack);
			} else {
				trackAndAddIfDuplicate(currPath, duplicates, seenFiles);
			}
		}

		return duplicates;
	}

	private void trackAndAddIfDuplicate(Path filePath, List<FilePaths> duplicates, Map<String, FileInfo> seenFiles) {
		try {
			String contents = sampleFileHash(filePath);
			if (seenFiles.containsKey(contents)) {
				FileInfo oldFileInfo = seenFiles.get(contents);
				long currLastModifiedTime = Files.getLastModifiedTime(filePath).toMillis();
				if (currLastModifiedTime < oldFileInfo.lastModifiedTime) {
					duplicates.add(new FilePaths(oldFileInfo.path, filePath));
					seenFiles.put(contents, new FileInfo(currLastModifiedTime, filePath));
				} else {
					duplicates.add(new FilePaths(filePath, oldFileInfo.path));
				}
			} else {
				seenFiles.put(contents, new FileInfo(Files.getLastModifiedTime(filePath).toMillis(), filePath));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static final int SAMPLE_SIZE = 4000;

	private String sampleFileHash(Path filePath) throws IOException {
		long totalBytes = Files.size(filePath);
		try (InputStream is = Files.newInputStream(filePath)) {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			DigestInputStream dis = new DigestInputStream(is, digest);

			if (totalBytes < SAMPLE_SIZE * 3) {
				dis.read(new byte[(int) totalBytes]);
			} else {
				byte[] bytes = new byte[SAMPLE_SIZE * 3];
				long numBytesToSkipBetweenSamples = (totalBytes - SAMPLE_SIZE * 3) / 2;
				for (int n = 0; n < 3; n++) {
					dis.read(bytes, n * SAMPLE_SIZE, SAMPLE_SIZE);
					dis.skip(numBytesToSkipBetweenSamples);
				}
			}
			return new BigInteger(1, digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	private void addContentsToStack(Path directory, Deque<Path> stack) {
		try (Stream<Path> dirStream = Files.list(directory)) {
			dirStream.forEach(path -> stack.push(path));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static class FileInfo {
		long lastModifiedTime;
		Path path;

		FileInfo(long lastModifiedTime, Path path) {
			this.lastModifiedTime = lastModifiedTime;
			this.path = path;
		}
	}

	public class FilePaths {
		private Path duplicatePath;
		private Path originalPath;

		public FilePaths(Path duplicatePath, Path originalPath) {
			this.duplicatePath = duplicatePath;
			this.originalPath = originalPath;
		}

		public Path getDuplicatePath() {
			return duplicatePath;
		}

		public Path getOriginalPath() {
			return originalPath;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((duplicatePath == null) ? 0 : duplicatePath.hashCode());
			result = prime * result + ((originalPath == null) ? 0 : originalPath.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			FilePaths other = (FilePaths) obj;
			if (duplicatePath == null) {
				if (other.duplicatePath != null)
					return false;
			} else if (!duplicatePath.equals(other.duplicatePath))
				return false;
			if (originalPath == null) {
				if (other.originalPath != null)
					return false;
			} else if (!originalPath.equals(other.originalPath))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return String.format("(duplicate: %s, original: %s)", duplicatePath, originalPath);
		}
	}
}
