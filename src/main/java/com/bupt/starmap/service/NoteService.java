package com.bupt.starmap.service;

import com.bupt.starmap.controller.utils.NoteRequest;
import com.bupt.starmap.domain.Note;

import java.util.List;

public interface NoteService {
  Note saveNote(NoteRequest noteRequest, String username);

  List<Note> getNotes(String username);

  List<Note> saveNotes(List<Note> nodes, String username);

  void deleteNote(Long noteId);
}
