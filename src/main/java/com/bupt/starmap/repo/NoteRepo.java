package com.bupt.starmap.repo;

import com.bupt.starmap.domain.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepo extends JpaRepository<Note, Long> {
  Note findByTitle(String title);
}
