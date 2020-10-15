package epam.com.esm.dao;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.dto.GiftCertificateDto;

import java.util.List;

public interface GiftCertificateDao extends CrudDAO<GiftCertificate> {

    List<GiftCertificateDto> getCertificatesByTagName(String tagName);
    List<GiftCertificateDto> getCertificatesByPartOfName(String partOfName);
    List<GiftCertificateDto> getCertificatesByPartOfDescription(String partOfDescription);
}
