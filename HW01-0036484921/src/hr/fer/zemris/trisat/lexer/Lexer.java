package hr.fer.zemris.trisat.lexer;

import java.util.HashMap;
import java.util.Map;

/**
 * Lexer used for converting the character stream to the token stream.
 * Rules:
 * 1) If line starts with letter 'c', then whole line is declared as comment(exempt from
 * processing).
 * 2) Number 0 represents end of line
 * 3) Character '%' represents end of file (characters after '%' are not included).
 * 4) String "cnf" represents format
 *
 * @author Dominik Stanojević
 * @version 1.0
 */
public class Lexer {
	/**
	 * program represented as character array
	 */
	private char[] data;
	/**
	 * current position in character array
	 */
	private int position = 0;
	/**
	 * current token
	 */
	private Token currentToken;

	private static Map<String, String> keywords = new HashMap<>();

	static {
		keywords.put("p", "declaration_line");
		keywords.put("cnf", "format");
	}

	/**
	 * Constructs new lexer with given program.
	 *
	 * @param program the program that needs to be analysed
	 */
	public Lexer(String program) {
		data = program.toCharArray();
		nextToken();
	}

	/**
	 * Finds next token.
	 */
	public void nextToken() {
		checkEOF();
		skipWhitespaces();

		if (scanEOF()) {
			currentToken = new Token(Token.TokenType.EOF, null);
			return;
		}

		if (scanComment()) {
			skipComment();
			return;
		}

		if (scanNumber()) {
			readNumber();
			return;
		}

		if (scanString()) {
			readString();
			return;
		}
	}

	/**
	 * Reads string.
	 */
	private void readString() {
		int start = position;
		position++;

		while (position < data.length && Character.isLetterOrDigit(data[position])) {
			position++;
		}
		int end = position;

		String string = new String(data, start, end - start);
		if (!keywords.containsKey(string)) {
			throw new RuntimeException("Illegal lexeme " + string);
		} else {
			currentToken = new Token(Token.TokenType.KEYWORD, keywords.get(string));
		}
	}

	/**
	 * Scans if current character is a letter, except 'c'.
	 *
	 * @return true if current character is a letter, otherwise false
	 */
	private boolean scanString() {
		if (Character.isLetter(data[position])) {
			return true;
		}

		return false;
	}

	/**
	 * Scans if current character is '%'.
	 *
	 * @return ture if current character is '%', otherwise false
	 */
	private boolean scanEOF() {
		if (data[position] == '%') {
			return true;
		}
		return false;
	}

	/**
	 * Reads number.
	 */
	private void readNumber() {
		boolean isNegative = false;
		if (data[position] == '-') {
			isNegative = true;
			position++;
		}

		String number = isNegative ? "-" : "";
		while (position < data.length && Character.isDigit(data[position])) {
			number += data[position];
			position++;
		}

		int num = Integer.parseInt(number);
		if (num == 0) {
			currentToken = new Token(Token.TokenType.EOL, null);
		} else {
			currentToken = new Token(Token.TokenType.NUMBER, num);
		}
	}

	/**
	 * Scans if current character is a digit or a minus sign.
	 *
	 * @return true if current sign is a digit or a misnus sign.
	 */
	private boolean scanNumber() {
		if (position < data.length &&
			(data[position] == '-' || Character.isDigit(data[position]))) {
			return true;
		}

		return false;
	}

	/**
	 * Skips comment line.
	 */
	private void skipComment() {
		while (position < data.length && !(data[position] == '\n' || data[position] == '\r')) {
			position++;
		}

		if (data[position] == '\r') {
			position += 2;
		} else {
			position++;
		}

		nextToken();
	}

	/**
	 * Returns true if character 'c' is found. Whole line is then interpreted as comment.
	 *
	 * @return true if character 'c' is found followed by space character, otherwise false
	 */
	private boolean scanComment() {
		if (data[position] != 'c') {
			return false;
		}

		int tempPos = position + 1;
		if (tempPos >= data.length || !Character.isWhitespace(data[tempPos])) {
			return false;
		}

		return true;
	}

	/**
	 * Skips all whitespace characters.
	 */
	private void skipWhitespaces() {
		while (position < data.length && Character.isWhitespace(data[position])) {
			position++;
		}
	}

	/**
	 * Checks if valid EOF is reached and if nextToken method has been called after the EOF
	 * token.
	 *
	 * @throws RuntimeException if EOF is not expected or if nextToken method is called
	 *                          after EOF
	 */
	private void checkEOF() {
		if (position >= data.length) {
			throw new RuntimeException("Unexpected EOF has been reached.");
		}
		if (currentToken != null && currentToken.getType() == Token.TokenType.EOF) {
			throw new RuntimeException("EOF already reached. No more token to produce.");
		}
	}

	/**
	 * Returns current token.
	 *
	 * @return current token
	 */
	public Token getCurrentToken() {
		return currentToken;
	}
}
