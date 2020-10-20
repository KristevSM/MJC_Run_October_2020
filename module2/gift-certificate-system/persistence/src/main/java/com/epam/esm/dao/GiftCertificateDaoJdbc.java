package com.epam.esm.dao;

import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.*;

@Repository
public class GiftCertificateDaoJdbc implements GiftCertificateDao {

    private static final String SQL_SELECT_ALL_CERTIFICATES = "SELECT gift_certificate.id, gift_certificate.name, description, \n" +
            "price, create_date, last_update_date, duration, tag.id as tag_id, tag.name as tag_name \n" +
            "FROM gift_certificate \n" +
            "left JOIN tag_has_gift_certificate on (gift_certificate.id=tag_has_gift_certificate.gift_certificate_id)\n" +
            "left JOIN tag on (tag.id=tag_has_gift_certificate.tag_id);";
    private static final String SQL_INSERT_GIFT_CERTIFICATE = "INSERT INTO gift_certificate (name, description, price, duration, create_date, last_update_date)" +
            " VALUES (:name, :description, :price, :duration, :createDate, :lastUpdateDate);";
    private static final String SQL_SELECT_BY_ID = "SELECT gift_certificate.id, gift_certificate.name, \n" +
            "            description, price, create_date, \n" +
            "            last_update_date, duration, tag.id as tag_id, tag.name as tag_name\n" +
            "            FROM gift_certificate \n" +
            "            JOIN tag_has_gift_certificate on (gift_certificate.id=tag_has_gift_certificate.gift_certificate_id)\n" +
            "            JOIN tag on (tag.id=tag_has_gift_certificate.tag_id) where gift_certificate.id = :id;";
    private static final String SQL_UPDATE_GIFT_CERTIFICATE = "UPDATE gift_certificate SET name = :name, description = :description," +
            " price = :price, create_date = :createDate, last_update_date = :lastUpdateDate WHERE (id = :id);";
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
    private static final String SQL_ADD_TAG_TO_CERTIFICATE = "INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id)" +
            " VALUES (:gift_certificate_id, :tag_id);" ;
    private static final String SQL_REMOVE_TAG_FROM_CERTIFICATE = "DELETE FROM tag_has_gift_certificate WHERE " +
            "(tag_id = :tag_id) and (gift_certificate_id = :gift_certificate_id);";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public GiftCertificateDaoJdbc(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private final ResultSetExtractor<List<GiftCertificate>> resultSetExtractor = resultSet -> {
        Map<Long, GiftCertificate> giftCertificateMap = new HashMap<>();
        Long id = 0L;
        while (resultSet.next()) {
            if (!giftCertificateMap.containsKey(resultSet.getLong("id"))) {
                id = resultSet.getLong("id");
                GiftCertificate certificate = GiftCertificate.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .description(resultSet.getString("description"))
                        .price(resultSet.getBigDecimal("price"))
                        .createDate(resultSet.getTimestamp("create_date").toInstant().atZone(ZoneId.systemDefault()))
                        .lastUpdateDate(resultSet.getTimestamp("last_update_date").toInstant().atZone(ZoneId.systemDefault()))
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
            result = new ArrayList<>(giftCertificateMap.values());
            return result;
        } else {
            throw new GiftCertificateNotFoundException("The gift certificate not found");
        }
    };


    @Override
    public Optional<GiftCertificate> find(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<GiftCertificate> result = namedParameterJdbcTemplate.query(SQL_SELECT_BY_ID, params, resultSetExtractor);

        if (Objects.requireNonNull(result).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public Long save(GiftCertificate model) {
        MapSqlParameterSource parameters = new MapSqlParameterSource().addValue("name", model.getName())
                .addValue("description", model.getDescription())
                .addValue("price", model.getPrice())
                .addValue("createDate", Timestamp.from(model.getCreateDate().toInstant()))
                .addValue("lastUpdateDate", Timestamp.from(model.getLastUpdateDate().toInstant()))
                .addValue("duration", model.getDuration());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQL_INSERT_GIFT_CERTIFICATE, parameters, keyHolder, new String[]{"id"});
        if (keyHolder.getKey() == null) {
            throw new IllegalArgumentException("Saving certificate failed, no ID obtained.");
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
        params.put("createDate", Timestamp.from(model.getCreateDate().toInstant()));
        params.put("lastUpdateDate", Timestamp.from(model.getLastUpdateDate().toInstant()));
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
        return namedParameterJdbcTemplate.query(SQL_SELECT_ALL_CERTIFICATES, resultSetExtractor);
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

    @Override
    public void addTagToCertificate(Long certificateId, Long tagId) {
        Map<String, Object> params = new HashMap<>();
        params.put("gift_certificate_id", certificateId);
        params.put("tag_id", tagId);
        namedParameterJdbcTemplate.update(SQL_ADD_TAG_TO_CERTIFICATE, params);
   }

    @Override
    public void removeTagFromCertificate(Long certificateId, Long tagId) {
        Map<String, Object> params = new HashMap<>();
        params.put("gift_certificate_id", certificateId);
        params.put("tag_id", tagId);
        namedParameterJdbcTemplate.update(SQL_REMOVE_TAG_FROM_CERTIFICATE, params);
    }


}
