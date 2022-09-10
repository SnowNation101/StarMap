package com.bupt.starmap.controller;

import com.bupt.starmap.domain.Note;
import com.bupt.starmap.repo.NoteRepo;
import com.bupt.starmap.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/note")
@RequiredArgsConstructor
public class NoteController {
    @Autowired
    private NoteRepo noteRepo;
    private NoteService noteService;

    @GetMapping("/findAll/{page}/{size}")
    public Page<Note> findall(@PathVariable("page") Integer page, @PathVariable("size") Integer size) {
        PageRequest request = PageRequest.of(page, size);
        return noteRepo.findAll(request);
    }

    @PostMapping("/save")
    public ResponseEntity<Note> saveNote(@RequestBody Note note) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/note/save").toUriString());
        return ResponseEntity.created(uri).body(noteService.saveNote(note));
    }

    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable("noteId") Long noteId) {
        noteService.deleteNote(noteId);
    }

}