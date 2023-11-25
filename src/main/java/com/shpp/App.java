package com.shpp;

import java.sql.*;
import javax.sql.DataSource;

import com.shpp.dop.SQLTestLoadingBulk;
import com.shpp.query.QueryExecutor;
import org.apache.commons.lang3.time.StopWatch;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//commit?
public class App {
    private static final Logger LOGGER = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        String categoryName = System.getProperty("category", "Category_3");
        LOGGER.info("Start program");
        DatabaseInitializer databaseInitializer = new DatabaseInitializer();
        DataInserting insertDataPreparedStatement = new DataInserting();
        QueryExecutor queryExecutor = new QueryExecutor(categoryName);
        IndexCreator indexCreator = new IndexCreator();
        StopWatch stopWatch = new StopWatch();

        databaseInitializer.createTables();
        stopWatch.start();
        insertDataPreparedStatement.insertStores();
        insertDataPreparedStatement.insertProductCategories();
        //insertDataPreparedStatement.insertProducts();
        SQLTestLoadingBulk sqlTestLoadingBulk = new SQLTestLoadingBulk();
        sqlTestLoadingBulk.insertData();
        insertDataPreparedStatement.insertDeliveries();
        LOGGER.info("Data generation completed");

        stopWatch.stop();
        LOGGER.info("Generation and insertion time: " + stopWatch.getTime() + " ms");

        stopWatch.reset();
        stopWatch.start();
        queryExecutor.querySQL();
        stopWatch.stop();
        LOGGER.info("Query time without indexes: " + stopWatch.getTime() + " ms");

        indexCreator.createIndex();
        stopWatch.reset();
        stopWatch.start();
        queryExecutor.querySQL();
        stopWatch.stop();
        LOGGER.info("Query time with indexes: " + stopWatch.getTime() + " ms");

    }

}
//SELECT * , стовбці і вирази
//[FROM таблиця
//JOIN таблиця ON умова]
//[WHERE умова]
//[ GROUP BY стовбці]
//[HAVING умова]
//[ORDER BY стовбці]

//Приклад 3.1. Процедура без параметрів. Розробити процедуру для отримання назв і
//вартості товарів, придбаних Івановим.
//CREATE PROC
//my_proc1 AS
//SELECT Товар.Найменування,
//Товар.Ціна * Угода.Кількість
//AS Вартість, Клієнт.
//FROM Клієнт INNER JOIN
//(Товар INNER JOIN Угода
//ON Товар.КодТовара = Угода.КодТовара)
//ON Клієнт.КодКлієнта = Угода.КодК

//	@Override
//	public void run(ApplicationArguments args) throws Exception {
//		logger.debug("Populating the database with 20K Dummy Records ... In Progress");
//		List<Student> students = new ArrayList<>();
//
//		IntStream.range(1,20000).forEach(id -> {
//			Student student = new Student();
//			student.setFirstName("f" + id);
//			student.setLastName("l" + id);
//			student.setEmail("email@" + id + "email.com");
//
//			int randomClass = ThreadLocalRandom.current().nextInt(1, 12 + 1);
//
//			student.setKlass(randomClass);
//
//			students.add(student);
//		});
//
//		studentService.saveAll(students);
//
//		logger.debug("Database filled up!");
//	}