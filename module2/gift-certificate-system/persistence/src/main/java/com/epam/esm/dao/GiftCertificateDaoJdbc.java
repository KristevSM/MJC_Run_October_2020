package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class GiftCertificateDaoJdbc implements GiftCertificateDao {

    //todo fix sql
    private static final String SQL_SELECT_ALL_CERTIFICATES = "SELECT id, name, description, create_date, last_update_date," +
            " price, duration FROM gift_certificate;";
    private static final String SQL_INSERT_GIFT_CERTIFICATE = "INSERT INTO gift_certificate (name, description, price, duration)" +
            " VALUES (:name, :description, :price, :duration);";
    private static final String SQL_SELECT_BY_ID = "SELECT id, name, description, create_date, last_update_date, " +
            "price, duration FROM gift_certificate WHERE id = :id;";
    private static final String SQL_UPDATE_GIFT_CERTIFICATE = "UPDATE gift_certificate SET name = :name, description = :description," +
            " price = :price, create_date = :createDate WHERE (id = :id);";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM gift_certificate WHERE (id = :id);";
    private static final String SQL_SELECT_CERTIFICATES_BY_TAG_NAME = "SELECT gift_certificate.id, gift_certificate.name, " +
            "description, price, create_date, \n" +
            "last_update_date, duration, tag.id as tag_id, tag.name as tag_name\n" +
            "FROM gift_certificate \n" +
            "JOIN tag_has_gift_certificate on (gift_certificate.id=tag_has_gift_certificate.gift_certificate_id)\n" +
            "JOIN tag on (tag.id=tag_has_gift_certificate.tag_id) WHERE tag.name = :tag_name;";
    private static final String SQL_SELECT_CERTIFICATES_BY_PART_OF_NAME = "SELECT gift_certificate.id, gift_certificate.name, " +
            "description, price, create_date, \n" +
            "last_update_date, duration, tag.id as tag_id, tag.name as tag_name\n" +
            "FROM gift_certificate \n" +
            "JOIN tag_has_gift_certificate on (gift_certificate.id=tag_has_gift_certificate.gift_certificate_id)\n" +
            "JOIN tag on (tag.id=tag_has_gift_certificate.tag_id) WHERE gift_certificate.name like :part_name;";
    private static final String SQL_SELECT_CERTIFICATES_BY_PART_OF_DESCRIPTION = "SELECT gift_certificate.id, gift_certificate.name, " +
            "description, price, create_date, \n" +
            "last_update_date, duration, tag.id as tag_id, tag.name as tag_name\n" +
            "FROM gift_certificate \n" +
            "JOIN tag_has_gift_certificate on (gift_certificate.id=tag_has_gift_certificate.gift_certificate_id)\n" +
            "JOIN tag on (tag.id=tag_has_gift_certificate.tag_id) WHERE description like :part_description;";


    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GiftCertificateDaoJdbc(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private final RowMapper<GiftCertificate> giftCertificateRowMapper =
            JdbcTemplateMapperFactory.newInstance().newRowMapper(GiftCertificate.class);

//    private final RowMapper<GiftCertificate> giftCertificateRowMapperSimple = (resultSet, i) -> GiftCertificate.builder()
//            .id(resultSet.getLong("id"))
//            .name(resultSet.getString("name"))
//            .description(resultSet.getString("description"))
//            .price(resultSet.getBigDecimal("price"))
//            .createDate(LocalDateTime.from(Instant.ofEpochMilli(resultSet.getDate("create_date").getTime())
//                    .atZone(ZoneId.systemDefault())
//                    .toLocalDateTime()))
//            .createDate(LocalDateTime.from(Instant.ofEpochMilli(resultSet.getDate("last_update_date").getTime())
//                    .atZone(ZoneId.systemDefault())
//                    .toLocalDateTime()))
//            .duration(resultSet.getInt("duration"))
//            .build();

    private final ResultSetExtractor<List<GiftCertificate>> resultSetExtractor = resultSet -> {
        Map<Long, GiftCertificate> giftCertificateMap = new HashMap<>();
        Long id = 0L;
        while (resultSet.next()) {
            if (!giftCertificateMap.containsKey(id)) {
                id = resultSet.getLong("id");
                GiftCertificate certificate = GiftCertificate.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .description(resultSet.getString("description"))
                        .price(resultSet.getBigDecimal("price"))
                        .createDate(LocalDateTime.from(Instant.ofEpochMilli(resultSet.getDate("create_date").getTime())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()))
                        .createDate(LocalDateTime.from(Instant.ofEpochMilli(resultSet.getDate("last_update_date").getTime())
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()))
                        .duration(resultSet.getInt("duration"))
                        .tags(new ArrayList<>())
                        .build();
                giftCertificateMap.put(id, certificate);
            }
            Tag tag = Tag.builder()
                    .id(resultSet.getLong("tag_id"))
                    .name(resultSet.getString("tag_name"))
                    .build();
            giftCertificateMap.get(id).getTags().add(tag);
        }

        List<GiftCertificate> result;
        if (!giftCertificateMap.isEmpty()) {
            result = new ArrayList<>();
            result.add(giftCertificateMap.get(id));
            return result;
        } else {
            throw new NoSuchElementException("The element not found");
        }
    };


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
        namedParameterJdbcTemplate.update(SQL_INSERT_GIFT_CERTIFICATE, parameters, keyHolder, new String[]{"id"});
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

    @Override
    public List<GiftCertificate> getCertificatesByTagName(String tagName) {
        Map<String, Object> params = new HashMap<>();
        params.put("tag_name", tagName);
        return namedParameterJdbcTemplate.query(SQL_SELECT_CERTIFICATES_BY_TAG_NAME, params, resultSetExtractor);
    }

    @Override
    public List<GiftCertificate> getCertificatesByPartOfName(String partOfName) {
        Map<String, Object> params = new HashMap<>();
        partOfName = "%" + partOfName + "%";
        params.put("part_name", partOfName);
        return namedParameterJdbcTemplate.query(SQL_SELECT_CERTIFICATES_BY_PART_OF_NAME, params, resultSetExtractor);
    }

    @Override
    public List<GiftCertificate> getCertificatesByPartOfDescription(String partOfDescription) {
        Map<String, Object> params = new HashMap<>();
        partOfDescription = "%" + partOfDescription + "%";
        params.put("part_description", partOfDescription);
        return namedParameterJdbcTemplate.query(SQL_SELECT_CERTIFICATES_BY_PART_OF_DESCRIPTION, params, resultSetExtractor);

    }
}
