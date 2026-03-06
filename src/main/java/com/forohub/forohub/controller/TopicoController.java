package com.forohub.forohub.controller;

import com.forohub.forohub.domain.topico.DatosActualizacionTopico;
import com.forohub.forohub.domain.topico.DatosRegistroTopico;
import com.forohub.forohub.domain.topico.DatosRespuestaTopico;
import com.forohub.forohub.domain.topico.DatosListadoTopico;
import com.forohub.forohub.domain.topico.Topico;
import com.forohub.forohub.domain.topico.TopicoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@Validated
@RequestMapping("/topicos")
public class TopicoController {

    private final TopicoRepository topicoRepository;

    public TopicoController(TopicoRepository topicoRepository) {
        this.topicoRepository = topicoRepository;
    }

    @GetMapping
    public List<DatosListadoTopico> listarTopicos() {
        return topicoRepository.findAll(Sort.by(Sort.Direction.ASC, "fechaCreacion"))
                .stream()
                .map(DatosListadoTopico::new)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaTopico> detallarTopico(@PathVariable @Positive Long id) {
        return topicoRepository.findById(id)
                .map(topico -> ResponseEntity.ok(new DatosRespuestaTopico(topico)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(
            @PathVariable @Positive Long id,
            @RequestBody @Valid DatosActualizacionTopico datos
    ) {
        var topicoOptional = topicoRepository.findById(id);
        if (!topicoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        var topicoDuplicado = topicoRepository.findByTituloAndMensaje(datos.titulo(), datos.mensaje());
        if (topicoDuplicado.isPresent() && !topicoDuplicado.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un topico con el mismo titulo y mensaje");
        }

        var topico = topicoOptional.get();
        topico.actualizarDatos(datos.titulo(), datos.mensaje(), datos.autor(), datos.curso());
        return ResponseEntity.ok(new DatosRespuestaTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarTopico(@PathVariable @Positive Long id) {
        var topicoOptional = topicoRepository.findById(id);
        if (!topicoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(
            @RequestBody @Valid DatosRegistroTopico datos,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        if (topicoRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un topico con el mismo titulo y mensaje");
        }

        var topico = new Topico(datos.titulo(), datos.mensaje(), datos.autor(), datos.curso());
        topicoRepository.save(topico);

        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body(new DatosRespuestaTopico(topico));
    }
}
