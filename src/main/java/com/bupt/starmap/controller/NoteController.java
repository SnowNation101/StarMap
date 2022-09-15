package com.bupt.starmap.controller;

import com.bupt.starmap.controller.utils.NoteRequest;
import com.bupt.starmap.domain.Note;
import com.bupt.starmap.repo.NoteRepo;
import com.bupt.starmap.service.NoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/note")
@RequiredArgsConstructor
public class NoteController {
  @Autowired
  private NoteService noteService;
  private NoteRepo noteRepo;

  @GetMapping("/findAll/{page}/{size}")
  public Page<Note> findall(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
    PageRequest request = PageRequest.of(page, size);
    return noteRepo.findAll(request);
  }

  @PostMapping("/save")
  public ResponseEntity<Note> saveNote(@RequestBody NoteRequest noteRequest) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/note/save").toUriString());
    return ResponseEntity.created(uri).body(noteService.saveNote(noteRequest, username));
  }

  @DeleteMapping("/{noteId}")
  public void deleteNote(@PathVariable("noteId") Long noteId) {
    noteService.deleteNote(noteId);
  }

  @GetMapping("/get/all")
  public List<Note> getNotes() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return noteService.getNotes(username);
  }

  @PostMapping("/save/all")
  public ResponseEntity<List<Note>> saveNodes(@RequestBody List<Note> notes) {
    URI uri = URI.create(ServletUriComponentsBuilder
        .fromCurrentContextPath().path("/api/save/all").toUriString());
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.created(uri).body(noteService.saveNotes(notes, username));
  }

}
