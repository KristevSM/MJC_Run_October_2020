package epam.com.esm.dao;

import com.epam.esm.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GiftCertificateDaoJdbc implements GiftCertificateDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final String SQL_SELECT_ALL_CERTIFICATES = "SELECT * FROM gift_certificate;";
    private final String SQL_INSERT_GIFT_CERTIFICATE = "INSERT INTO gift_certificate (name, description, price, duration)" +
            " VALUES (:name, :description, :price, :duration);";

//    @Value("${gift_certificate.select_all}")
//    private String SQL_SELECT_ALL_CERTIFICATES;

    @Autowired
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
        return Optional.empty();
    }

    @Override
    public void save(GiftCertificate model) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", model.getName());
        params.put("description", model.getDescription());
        params.put("price", model.getPrice());
        params.put("duration", model.getDuration());
        namedParameterJdbcTemplate.update(SQL_INSERT_GIFT_CERTIFICATE, params);
    }

    @Override
    public void update(GiftCertificate model) {

    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<GiftCertificate> findAll() {
        return  namedParameterJdbcTemplate.query(SQL_SELECT_ALL_CERTIFICATES, giftCertificateRowMapper);
    }
}
