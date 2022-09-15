package com.bupt.starmap.service;

import com.bupt.starmap.controller.utils.NoteRequest;
import com.bupt.starmap.domain.Note;
import com.bupt.starmap.repo.NoteRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteRepo noteRepo;

    @Override
    public Note saveNote(NoteRequest noteRequest, String username) {
        Note note = new Note();
        note.setUsername(username);
        note.setCreateTime(LocalDateTime.now());
        note.setTitle(noteRequest.getTitle());
        note.setContent(noteRequest.getContent());
        note.setCategory(noteRequest.getCategory());
        log.info(note.toString());
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

    @Override
    public List<Note> getNotes(String username) {
        return noteRepo.findAllByUsername(username);
    }

    @Override
    public List<Note> saveNotes(List<Note> nodes, String username) {
        noteRepo.deleteAllByUsername(username);
        return noteRepo.saveAll(nodes);
    }

}
