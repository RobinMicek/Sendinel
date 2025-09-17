package cz.sendinel.api.util;

import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.Node;
import cz.jirutka.rsql.parser.ast.RSQLOperators;
import cz.sendinel.api.rsql.CustomRsqlVisitor;
import org.springframework.data.jpa.domain.Specification;

import java.util.HashSet;
import java.util.Set;

public class RsqlUtil {

    private static final Set<ComparisonOperator> OPERATORS;

    static {
        OPERATORS = new HashSet<>(RSQLOperators.defaultOperators());
        OPERATORS.add(new ComparisonOperator("=like=", true)); // add LIKE support
    }

    public static <T> Specification<T> toSpecification(String search) {
        if (search == null || search.isBlank()) {
            return null;
        }
        Node rootNode = new RSQLParser(OPERATORS).parse(search);
        return rootNode.accept(new CustomRsqlVisitor<>());
    }
}