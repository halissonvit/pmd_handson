package br.ufg.inf.vv.pmdhandson;

import net.sourceforge.pmd.PMD;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class PMDExecutor {
	private String sourceFolder;
	private OutputStream outputStream;
	private OutputStream errorOutputStream;
	private List<String> errors;

	private PrintStream defaultOutputStream;
	private PrintStream defaultErrorOutputStream;

	public PMDExecutor(String directory) {
		this.sourceFolder = directory;
		this.outputStream = new ByteArrayOutputStream();
		this.errorOutputStream = new ByteArrayOutputStream();
		this.errors = new ArrayList<String>();
	}

	public OutputStream getOutputStream() {
		return this.outputStream;
	}

	public OutputStream getErrorOutputStream() {
		return this.errorOutputStream;
	}

	public List getErrors() {
		return this.errors;
	}

	private String[] getArgumentsForPMD() throws UnsupportedEncodingException {
		// Init the arguments
		String filePath = new File(sourceFolder).getAbsoluteFile().toString();
		String outputType = "text";
		String rules = URLDecoder.decode(new File("pmdrules.xml").toString(), "UTF-8");
		return new String[]{filePath, outputType, rules};
	}

	private void hookStreams() {
		// Save the streams to be restored after the test
		this.defaultOutputStream = System.out;
		this.defaultErrorOutputStream = System.err;

		PrintStream psOut = new PrintStream(outputStream);
		PrintStream psErr = new PrintStream(errorOutputStream);

		// Hook the streams with our own
		System.setOut(psOut);
		System.setErr(psErr);
	}

	private void closeStreams() throws IOException {
		// Restore the default streams
		System.setOut(defaultOutputStream);
		System.setErr(defaultErrorOutputStream);
		outputStream.close();
		errorOutputStream.close();
	}

	private void formatErrors() {
		// Organize the output from the PMD test
		String linesOut[] = outputStream.toString().split("\\r?\\n");

		for (String line : linesOut) {
			if (line.length() > 0 &&
					line.indexOf("suppressed by Annotation") == -1 &&
					line.indexOf("No problems found!") == -1 &&
					line.indexOf("Error while processing") == -1) {
				errors.add(line);
			}
		}
	}

	public void analise() throws IOException {
		String[] arguments = getArgumentsForPMD();
		hookStreams();
		// Star the actual PMD test
		PMD.main(arguments);
		closeStreams();
		formatErrors();
	}
}
