package epam.com.esm.dao;

import com.epam.esm.model.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GiftCertificateDaoJdbc implements GiftCertificateDao {

    private static final String SQL_SELECT_ALL_CERTIFICATES = "SELECT id, name, description, create_date, last_update_date," +
            " price, duration FROM gift_certificate;";
    private static final String SQL_INSERT_GIFT_CERTIFICATE = "INSERT INTO gift_certificate (name, description, price, duration)" +
            " VALUES (:name, :description, :price, :duration);";
    private static final String SQL_SELECT_BY_ID = "SELECT id, name, description, create_date, last_update_date, " +
            "price, duration FROM gift_certificate WHERE id = :id;";
    private static final String SQL_UPDATE_GIFT_CERTIFICATE = "UPDATE gift_certificate SET name = :name, description = :description," +
            " price = :price, create_date = :createDate WHERE (id = :id);";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM gift_certificate WHERE (id = :id);";


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public GiftCertificateDaoJdbc(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private final RowMapper<GiftCertificate> giftCertificateRowMapper = (resultSet, i) -> GiftCertificate.builder()
            .id(resultSet.getLong("id"))
            .name(resultSet.getString("name"))
            .description(resultSet.getString("description"))
            .price(resultSet.getBigDecimal("price"))
            .createDate(LocalDateTime.parse(resultSet.getString("create_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .lastUpdateDate(LocalDateTime.parse(resultSet.getString("last_update_date"), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .duration(resultSet.getInt("duration"))
            .build();

    @Override
    public Optional<GiftCertificate> find(Long id) {

        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<GiftCertificate> result = namedParameterJdbcTemplate.query(SQL_SELECT_BY_ID, params, giftCertificateRowMapper);

        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public Long save(GiftCertificate model) {
        MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("name", model.getName())
                .addValue("description", model.getDescription())
                .addValue("price", model.getPrice())
                .addValue("duration", model.getDuration());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQL_INSERT_GIFT_CERTIFICATE, parameters, keyHolder);
        if (keyHolder.getKey() == null) {
            throw new NoSuchElementException("Saving certificate failed, no ID obtained.");
        } else {
            return keyHolder.getKey().longValue();
        }
    }

    @Override
    public void update(GiftCertificate model) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", model.getId());
        params.put("name", model.getName());
        params.put("description", model.getDescription());
        params.put("price", model.getPrice());
        params.put("createDate", model.getCreateDate());
        params.put("lastUpdateDate", LocalDateTime.now());
        params.put("duration", model.getDuration());
        namedParameterJdbcTemplate.update(SQL_UPDATE_GIFT_CERTIFICATE, params);
    }

    //cascade deleting?
    @Override
    public void delete(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        namedParameterJdbcTemplate.update(SQL_DELETE_BY_ID, params);
    }

    @Override
    public List<GiftCertificate> findAll() {
        return namedParameterJdbcTemplate.query(SQL_SELECT_ALL_CERTIFICATES, giftCertificateRowMapper);
    }
}
