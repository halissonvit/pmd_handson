package br.ufg.inf.vv.pmdhandson;

public class BadCode {
	public void badMethod() {
		String unusedString = "this is an unsued variable";
		try {
			throw new Exception();
		} catch (Exception ex) {
		}
	}
}
