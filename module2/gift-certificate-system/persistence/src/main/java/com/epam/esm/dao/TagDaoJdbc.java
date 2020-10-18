package com.epam.esm.dao;

import com.epam.esm.model.Tag;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class TagDaoJdbc implements TagDao {

    private static final String SQL_SELECT_ALL_TAGS = "SELECT id, name FROM tag;";
    private static final String SQL_INSERT_TAG = "INSERT INTO tag (name)" +
            " VALUES (:name);";
    private static final String SQL_SELECT_BY_ID = "SELECT id, name FROM tag WHERE id = :id;";
    private static final String SQL_UPDATE_TAG = "UPDATE tag SET name = :name WHERE (id = :id);";
    private static final String SQL_DELETE_BY_ID = "DELETE FROM tag WHERE (id = :id);";
    private static final String SQL_SELECT_BY_TAG_NAME = "SELECT id, name FROM tag WHERE name = :name;";
    private static final String SQL_ASSIGN_DEFAULT_TAG = "INSERT INTO tag_has_gift_certificate (tag_id, gift_certificate_id) " +
            "VALUES (:tag_id, :gift_certificate_id)";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TagDaoJdbc(DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private final RowMapper<Tag> tagRowMapper = (resultSet, i) -> Tag.builder()
            .id(resultSet.getLong("id"))
            .name(resultSet.getString("name"))
            .build();

    @Override
    public Optional<Tag> find(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        List<Tag> result = namedParameterJdbcTemplate.query(SQL_SELECT_BY_ID, params, tagRowMapper);

        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public Long save(Tag model) {
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", model.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(SQL_INSERT_TAG, parameters, keyHolder, new String[]{"id"});
        if (keyHolder.getKey() == null) {
            throw new NoSuchElementException("Saving tag failed, no ID obtained.");
        } else {
            return keyHolder.getKey().longValue();
        }
    }

    @Override
    public void update(Tag model) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", model.getId());
        params.put("name", model.getName());
        namedParameterJdbcTemplate.update(SQL_UPDATE_TAG, params);
    }

    @Override
    public void delete(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        namedParameterJdbcTemplate.update(SQL_DELETE_BY_ID, params);
    }

    @Override
    public List<Tag> findAll() {
        return namedParameterJdbcTemplate.query(SQL_SELECT_ALL_TAGS, tagRowMapper);
    }

    @Override
    public Optional<Tag> findByTagName(String tagName) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", tagName);
        List<Tag> result = namedParameterJdbcTemplate.query(SQL_SELECT_BY_TAG_NAME, params, tagRowMapper);

        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public void assignTag(Long tagId, Long certificateId) {
        Map<String, Object> params = new HashMap<>();
        params.put("tag_id", tagId);
        params.put("gift_certificate_id", certificateId);
        namedParameterJdbcTemplate.update(SQL_ASSIGN_DEFAULT_TAG, params);
    }
}
