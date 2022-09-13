package com.bupt.starmap.service;

import com.bupt.starmap.domain.Note;
import com.bupt.starmap.repo.NoteRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NoteServiceImpl implements NoteService {

    private NoteRepo noteRepo;

    @Override
    public Note saveNote(Note note) {
        note.setCreateTime(LocalDateTime.now());
        log.info("Save new note {} to the database", note.getTitle());
        return noteRepo.save(note);
    }

    @Override
    public void deleteNote(Long id) {
        boolean exists = noteRepo.existsById(id);
        if (!exists) {
            throw new IllegalStateException(
                    "Note with ID " + id + "does not exist"
            );
        }
        log.info("Delete note {} from the database", noteRepo.findById(id).get().getTitle());
        noteRepo.deleteById(id);
    }
}
