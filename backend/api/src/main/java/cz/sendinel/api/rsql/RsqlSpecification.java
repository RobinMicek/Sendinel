package cz.sendinel.api.rsql;

import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class RsqlSpecification<T> implements Specification<T> {

    private final String property;
    private final String operator;
    private final List<String> arguments;

    public RsqlSpecification(String property, String operator, List<String> arguments) {
        this.property = property;
        this.operator = operator;
        this.arguments = arguments;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        if (arguments == null || arguments.isEmpty()) {
            throw new IllegalArgumentException("No arguments provided for property " + property);
        }

        Path<?> path = root.get(property);
        String value = arguments.get(0);

        // Handle nulls explicitly
        if (value.equalsIgnoreCase("null")) {
            return switch (operator) {
                case "==" -> cb.isNull(path);
                case "!=" -> cb.isNotNull(path);
                default -> throw new IllegalArgumentException("Unsupported operator with null: " + operator);
            };
        }

        // Handle String vs non-String
        if (path.getJavaType().equals(String.class)) {
            String lowered = value.toLowerCase();

            return switch (operator) {
                case "==" -> cb.equal(cb.lower(root.get(property)), lowered);
                case "!=" -> cb.notEqual(cb.lower(root.get(property)), lowered);
                case "=like=" -> {
                    String pattern = lowered.replace('*', '%');
                    yield cb.like(cb.lower(root.get(property)), pattern);
                }
                case "=in=" -> root.get(property).in(arguments.stream()
                        .map(String::toLowerCase)
                        .toList());
                default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
            };
        } else {
            // Non-string fields (numbers, enums, dates, etc.)
            return switch (operator) {
                case "==" -> cb.equal(path, value);
                case "!=" -> cb.notEqual(path, value);
                case "=in=" -> path.in(arguments);
                default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
            };
        }
    }

}
