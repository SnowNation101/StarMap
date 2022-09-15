package com.bupt.starmap.repo;

import com.bupt.starmap.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepo extends JpaRepository<Note, Long> {
  Note findByTitle(String title);

  List<Note> findAllByUsername(String username);

  void deleteAllByUsername(String username);
}
