/*
 * Copyright (C) 2022 David "SnowNation" Zhang
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License
 *  is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  or implied. See the License for the specific language governing permissions and limitations under
 *  the License.
 */

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

/**
 * The Note Controller
 * @author David Zhang
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/api/note")
@RequiredArgsConstructor
public class NoteController {
  @Autowired
  private NoteService noteService;
  private NoteRepo noteRepo;

  /**
   * Get notes in page
   * @param page
   * @param size
   * @return The specific {@code page} with {@code size} of notes
   */
  @GetMapping("/findAll/{page}/{size}")
  public Page<Note> findall(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
    PageRequest request = PageRequest.of(page, size);
    return noteRepo.findAll(request);
  }

  /**
   * Save a new note to the database
   * @param noteRequest
   * @return the note structure
   */
  @PostMapping("/save")
  public ResponseEntity<Note> saveNote(@RequestBody NoteRequest noteRequest) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/note/save").toUriString());
    return ResponseEntity.created(uri).body(noteService.saveNote(noteRequest, username));
  }

  /**
   * Delete note by {@code noteId}
   * @param noteId
   */
  @DeleteMapping("/{noteId}")
  public void deleteNote(@PathVariable("noteId") Long noteId) {
    noteService.deleteNote(noteId);
  }

  /**
   * Get all notes from database
   * @return List<Note> of all notes
   */
  @GetMapping("/get/all")
  public List<Note> getNotes() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return noteService.getNotes(username);
  }

  /**
   * Save all notes to the database
   * @param notes
   * @return Saved note info
   */
  @PostMapping("/save/all")
  public ResponseEntity<List<Note>> saveNodes(@RequestBody List<Note> notes) {
    URI uri = URI.create(ServletUriComponentsBuilder
        .fromCurrentContextPath().path("/api/note/save/all").toUriString());
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    return ResponseEntity.created(uri).body(noteService.saveNotes(notes, username));
  }

}
