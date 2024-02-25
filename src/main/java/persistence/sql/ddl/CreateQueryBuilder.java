package persistence.sql.ddl;

import java.util.stream.Collectors;
import persistence.sql.QueryBuilder;
import static persistence.sql.constant.SqlConstant.COMMA;
import persistence.sql.dialect.Dialect;
import persistence.sql.meta.Table;

public class CreateQueryBuilder implements QueryBuilder {

    private static final String CREATE_TABLE_DEFINITION = "CREATE TABLE %s (%s)";

    private final FieldQueryGenerator fieldBuilder;

    private CreateQueryBuilder(Dialect dialect) {
        fieldBuilder = FieldQueryGenerator.from(dialect);
    }

    public static CreateQueryBuilder from(Dialect dialect) {
        return new CreateQueryBuilder(dialect);
    }

    @Override
    public String generateQuery(Object object) {

        Table table = Table.from((Class<?>) object);

        String columnDefinitions = table.getColumns().stream()
            .map(fieldBuilder::generate)
            .collect(Collectors.joining(COMMA.getValue()));

        return String.format(CREATE_TABLE_DEFINITION, table.getTableName(), columnDefinitions);
    }
}