package hr.fer.zemris.trisat.parser;

import hr.fer.zemris.trisat.lexer.Lexer;
import hr.fer.zemris.trisat.lexer.Token;
import hr.fer.zemris.trisat.models.Clause;
import hr.fer.zemris.trisat.models.SATFormula;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dominik on 4.10.2016..
 */
public class Parser {
	private Lexer lexer;
	private SATFormula formula;

	public Parser(String program) {
		this.lexer = new Lexer(program);
		parse();
	}

	private void parse() {
		checkValue(Token.TokenType.KEYWORD, "declaration_line");
		lexer.nextToken();

		checkValue(Token.TokenType.KEYWORD, "format");
		lexer.nextToken();

		checkType(Token.TokenType.NUMBER);
		int numberOfVariables = (int) currentValue();
		lexer.nextToken();

		checkType(Token.TokenType.NUMBER);
		int numberOfClauses = (int) currentValue();
		lexer.nextToken();

		Clause[] clauses = new Clause[numberOfClauses];
		for (int i = 0; i < numberOfClauses; i++) {
			Clause clause = parseClause();
			clauses[i] = clause;
		}

		checkType(Token.TokenType.EOF);

		formula = new SATFormula(numberOfVariables, clauses);
	}

	private Clause parseClause() {
		List<Integer> variables = new ArrayList<>();

		while (lexer.getCurrentToken().getType() != Token.TokenType.EOL) {
			checkType(Token.TokenType.NUMBER);
			int value = (int) currentValue();

			variables.add(value);
			lexer.nextToken();
		}
		lexer.nextToken();

		int[] ints = variables.stream().mapToInt(i -> i.intValue()).toArray();
		return new Clause(ints);
	}

	private void checkType(Token.TokenType type) {
		if (lexer.getCurrentToken().getType() != type) {
			throw new RuntimeException("Expected type: " + type + ", given: " +
									   lexer.getCurrentToken().getType() + ".");
		}
	}

	private void checkValue(Token.TokenType type, Object compare) {
		checkType(type);

		if (compare != null && !currentValue().equals(compare)) {
			throw new RuntimeException(
					"Expected value: " + compare + ", given: " + currentValue());
		}
	}

	private Object currentValue() {
		return lexer.getCurrentToken().getValue();
	}

	public SATFormula getFormula() {
		return formula;
	}
}
