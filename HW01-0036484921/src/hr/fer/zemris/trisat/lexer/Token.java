package hr.fer.zemris.trisat.lexer;

/**
 * Token class represents token used for interpreting given data. It contains information
 * about lexeme's type and its value.
 *
 * @author Dominik Stanojević
 * @version 1.0
 */
public class Token {
	/**
	 * Enumeration representing token types.
	 */
	public enum TokenType {
		NUMBER, EOL, EOF, KEYWORD
	}

	/**
	 * token type
	 */
	private TokenType type;
	/**
	 * token value
	 */
	private Object value;

	/**
	 * Constructs new token based on its type and value.
	 * @param type lexeme's type
	 * @param value lexeme's value
	 */
	public Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}

	/**
	 * Returns lexeme's type
	 * @return lexeme's type
	 */
	public TokenType getType() {
		return type;
	}

	/**
	 * Returns lexeme's value
	 * @return lexeme's value
	 */
	public Object getValue() {
		return value;
	}
}
