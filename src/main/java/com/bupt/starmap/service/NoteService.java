package com.bupt.starmap.service;

import com.bupt.starmap.domain.Note;

public interface NoteService {
  Note saveNote(Note note);

    void deleteNote(Long noteId);
}
