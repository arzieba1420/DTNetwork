package pl.nazwa.arzieba.dtnetworkproject.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.nazwa.arzieba.dtnetworkproject.model.DownloadItem;

import java.util.List;
import java.util.Optional;

@Repository
public interface DownloadItemDAO extends JpaRepository<DownloadItem,Long> {
    List<DownloadItem> findAll();
    List<DownloadItem> findAllById(Long id);
    Optional<DownloadItem> findById(Long id);
    void deleteDownloadItemById(Long id);
}
