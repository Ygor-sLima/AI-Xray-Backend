package br.com.aixray.apixray.Repositories;

import br.com.aixray.apixray.Models.CountExameDTO;
import br.com.aixray.apixray.Models.Exame;
import org.socialsignin.spring.data.dynamodb.repository.DynamoDBCrudRepository;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.socialsignin.spring.data.dynamodb.repository.EnableScanCount;
import org.socialsignin.spring.data.dynamodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@EnableScan
@EnableScanCount
public interface ExameRepository extends DynamoDBCrudRepository<Exame, String> {
    Optional<Integer> countAllByDataRegistroStartsWith(String data);

    List<Exame> findAllByDataRegistroStartsWith(String data);

    Optional<Exame> findExameByIdExame(String idExame);

    List<Exame> getDistinctByResultado();
}
