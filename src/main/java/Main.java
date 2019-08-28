
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.EnumSet;

import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import com.datalogics.PDFL.*;

public class Main {

	static public void main(String[] args) {
		System.out.println("Modifying input.pdf");

		Library lib = null;
		try {
			lib = new Library();
		} catch (Throwable e) {
			System.err.println("Failed to load Adobe SDK");
			return;
		}

		Document doc = new Document();
		try (InputStream pdfContent = new FileInputStream(new File("input.pdf")); ImageInputStream inputStream = new MemoryCacheImageInputStream(pdfContent)) {
			Document pages = new Document(inputStream);

			Page page = pages.getPage(0);
			Content pageContent = page.getContent();

			/**
			 * When this line below is commented out, this issue won't occur.
			 */
			//page.updateContent();

			doc.insertPages(Document.LAST_PAGE, pages, 0, Document.ALL_PAGES, EnumSet.of(PageInsertFlags.NONE));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try (ImageOutputStream output = new MemoryCacheImageOutputStream(new FileOutputStream(new File("output.pdf")))) {
			doc.save(EnumSet.of(SaveFlags.FULL, SaveFlags.OPTIMIZE_CONTENT_STREAMS), output);
		} catch (Exception e) {
			e.printStackTrace();
		}

		lib.delete();
	}

}
