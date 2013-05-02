package br.ufg.inf.vv.pmdhandson.domain;

import br.ufg.inf.vv.pmdhandson.PMDExecutor;
import junit.framework.TestCase;
import org.junit.Test;

import java.util.List;

public class BadCodeTest extends TestCase {
	@Test
	public void testWithBadCode() throws Exception {
		doPMD("src");
	}

	private void doPMD(String sourceFolder) throws Exception {
		new PMDExecutor(sourceFolder).analise();
		// A friendly message informing we are going to start the test
		System.out.println("Starting PMD code analyzer test on directory '" + sourceFolder + "'..");

		PMDExecutor pmdExecutor = new PMDExecutor(sourceFolder);
		pmdExecutor.analise();

		List errors = pmdExecutor.getErrors();

		System.out.println("Found " + errors.size() + " errors");
		for (Object error : errors) {
			System.out.println(error);
		}

		assertTrue(errors.size() + " errors\n" + errors.toString(), errors.isEmpty());
		assertTrue(pmdExecutor.getErrorOutputStream().toString(), pmdExecutor.getErrorOutputStream().toString().trim().length() == 0);
	}
}

