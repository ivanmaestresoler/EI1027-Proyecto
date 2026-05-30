package es.uji.ei1027.proyecto.controller;

import es.uji.ei1027.proyecto.dao.ComunicacioUsuariOVIPAPDao;
import es.uji.ei1027.proyecto.dao.SeleccionDao;
import es.uji.ei1027.proyecto.dao.UsuarioDao;
import es.uji.ei1027.proyecto.model.ComunicacioUsuariOVIPAP;
import es.uji.ei1027.proyecto.model.Seleccion;
import es.uji.ei1027.proyecto.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/comunicacio")
public class ComunicacioController {

    private ComunicacioUsuariOVIPAPDao comunicacioDao;
    private SeleccionDao seleccionDao;
    private UsuarioDao usuarioDao;

    @Autowired
    public void setComunicacioDao(ComunicacioUsuariOVIPAPDao comunicacioDao) { this.comunicacioDao = comunicacioDao; }

    @Autowired
    public void setSeleccionDao(SeleccionDao seleccionDao) { this.seleccionDao = seleccionDao; }

    @Autowired
    public void setUsuarioDao(UsuarioDao usuarioDao) { this.usuarioDao = usuarioDao; }

    // ── LLISTA DE CONVERSATIONS ─────────────────────────────────────────────
    // Assistent veu les seues converses agrupades per selecció (usuari OVI)
    @GetMapping("/conversations")
    public String listConversations(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        List<Seleccion> seleccions;
        if (usuario.getTipusUsuari().equals("AssistentPersonal")) {
            seleccions = seleccionDao.getSeleccionsByAssistent(usuario.getIdUsuario());
        } else {
            seleccions = seleccionDao.getSeleccions();
        }

        List<Map<String, Object>> conversations = new ArrayList<>();
        for (Seleccion s : seleccions) {
            List<ComunicacioUsuariOVIPAP> missatges =
                    comunicacioDao.getComunicacionsBySeleccion(s.getIdSeleccion());
            if (!missatges.isEmpty()) {
                Map<String, Object> conv = new HashMap<>();
                conv.put("idSeleccion", s.getIdSeleccion());
                conv.put("idAssistent", s.getIdAssistent());

                // Nom de l'altre participant
                int idAltre = usuario.getTipusUsuari().equals("AssistentPersonal")
                        ? missatges.get(0).getIdFrom()
                        : s.getIdAssistent();
                Usuario altre = usuarioDao.getUsuario(idAltre);
                conv.put("nomAltre", altre != null ? altre.getNom() + " " + altre.getCognom1() : "Usuari #" + idAltre);
                conv.put("ultimMissatge", missatges.get(missatges.size() - 1).getMissatge());
                conv.put("dataUltim", missatges.get(missatges.size() - 1).getDataEnviament());
                conv.put("numMissatges", missatges.size());
                conversations.add(conv);
            }
        }

        model.addAttribute("conversations", conversations);
        return "comunicacio/conversations";
    }

    // ── XAT D'UNA CONVERSA ──────────────────────────────────────────────────
    @GetMapping("/xat/{idSeleccion}")
    public String xat(@PathVariable int idSeleccion, Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        List<ComunicacioUsuariOVIPAP> missatges =
                comunicacioDao.getComunicacionsBySeleccion(idSeleccion);

        // Preparar missatges amb noms
        List<Map<String, Object>> missatgesEnriquits = new ArrayList<>();
        for (ComunicacioUsuariOVIPAP m : missatges) {
            Map<String, Object> item = new HashMap<>();
            item.put("missatge", m.getMissatge());
            item.put("dataEnviament", m.getDataEnviament());
            item.put("esMeu", m.getIdFrom() == usuario.getIdUsuario());
            Usuario from = usuarioDao.getUsuario(m.getIdFrom());
            item.put("nomFrom", from != null ? from.getNom() + " " + from.getCognom1() : "Usuari #" + m.getIdFrom());
            missatgesEnriquits.add(item);
        }

        // Prepara nou missatge buit
        ComunicacioUsuariOVIPAP nouMissatge = new ComunicacioUsuariOVIPAP();
        nouMissatge.setIdSeleccion(idSeleccion);
        nouMissatge.setIdFrom(usuario.getIdUsuario());

        // id_to = l'altre participant
        Seleccion seleccion = seleccionDao.getSeleccion(idSeleccion);
        if (seleccion != null) {
            if (usuario.getTipusUsuari().equals("AssistentPersonal")) {
                // buscar l'usuari OVI que ha enviat algun missatge
                if (!missatges.isEmpty()) {
                    nouMissatge.setIdTo(missatges.get(0).getIdFrom());
                }
            } else {
                nouMissatge.setIdTo(seleccion.getIdAssistent());
            }
        }
        nouMissatge.setDataEnviament(LocalDateTime.now());

        model.addAttribute("missatges", missatgesEnriquits);
        model.addAttribute("nouMissatge", nouMissatge);
        model.addAttribute("idSeleccion", idSeleccion);
        return "comunicacio/xat";
    }

    // ── ENVIAR MISSATGE DES DEL XAT ─────────────────────────────────────────
    @PostMapping("/xat/{idSeleccion}")
    public String enviarMissatge(@PathVariable int idSeleccion,
                                 @ModelAttribute("nouMissatge") ComunicacioUsuariOVIPAP missatge,
                                 HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) return "redirect:/login";

        if (missatge.getIdSeleccion() == 0) missatge.setIdSeleccion(idSeleccion);
        missatge.setIdFrom(usuario.getIdUsuario());
        missatge.setDataEnviament(LocalDateTime.now());

        if (missatge.getMissatge() != null && !missatge.getMissatge().trim().isEmpty()) {
            comunicacioDao.addComunicacio(missatge);
        }

        return "redirect:/comunicacio/xat/" + idSeleccion;
    }

    // ── ADD SIMPLE (des del detalle de la petició) ──────────────────────────
    @GetMapping("/add")
    public String addComunicacio(Model model, HttpSession session,
                                 @RequestParam(required = false) Integer idSeleccion) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        ComunicacioUsuariOVIPAP comunicacio = new ComunicacioUsuariOVIPAP();

        if (idSeleccion != null) {
            comunicacio.setIdSeleccion(idSeleccion);
            if (usuario != null) comunicacio.setIdFrom(usuario.getIdUsuario());
            Seleccion seleccion = seleccionDao.getSeleccion(idSeleccion);
            if (seleccion != null) comunicacio.setIdTo(seleccion.getIdAssistent());
        }
        comunicacio.setDataEnviament(LocalDateTime.now());
        model.addAttribute("comunicacio", comunicacio);
        return "redirect:/comunicacio/xat/" + idSeleccion;
    }

    @PostMapping("/add")
    public String processAddSubmit(@ModelAttribute("comunicacio") ComunicacioUsuariOVIPAP comunicacio,
                                   HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (comunicacio.getIdSeleccion() == 0) return "redirect:/aprequest/list";
        if (usuario != null) comunicacio.setIdFrom(usuario.getIdUsuario());
        comunicacioDao.addComunicacio(comunicacio);
        return "redirect:/comunicacio/xat/" + comunicacio.getIdSeleccion();
    }

    @RequestMapping("/list")
    public String listComunicacions(Model model, @RequestParam(value = "page", defaultValue = "1") int page) {
        int pageSize = 5;
        int offset = (page - 1) * pageSize;

        int totalRecords = comunicacioDao.getTotalComunicacions();
        int totalPages = totalRecords == 0 ? 1 : (int) Math.ceil((double) totalRecords / pageSize);

        model.addAttribute("comunicacions", comunicacioDao.getComunicacionsPaginades(pageSize, offset));
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);

        return "comunicacio/list";
    }

    @GetMapping("/update/{id}")
    public String updateComunicacio(Model model, @PathVariable int id) {
        model.addAttribute("comunicacio", comunicacioDao.getComunicacio(id));
        return "comunicacio/update";
    }

    @PostMapping("/update")
    public String processUpdateSubmit(@ModelAttribute("comunicacio") ComunicacioUsuariOVIPAP comunicacio) {
        comunicacioDao.updateComunicacio(comunicacio);
        return "redirect:list";
    }

    @GetMapping("/delete/{id}")
    public String processDelete(@PathVariable int id) {
        comunicacioDao.deleteComunicacio(id);
        return "redirect:../list";
    }
}