package com.mola.cargo.controller;

import com.mola.cargo.model.Codepostal;
import com.mola.cargo.model.Emetteur;
import com.mola.cargo.model.Recepteur;
import com.mola.cargo.service.*;
import com.mola.cargo.util.Constante;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/personne")
public class RecepteurController {

    @Autowired
    private RecepteurService recepteurService;
    @Autowired
    private EmetteurService emetteurService;
    @Autowired
    private EtatService etatService;
    @Autowired
    private PaysService paysService;
    @Autowired
    private CommandeService commandeService;
    @Autowired
    private CodepostalService codepostalService;

    @GetMapping("/recepteurs")
    public String afficherRecepteur(Model model){
        model.addAttribute("recepteurs", recepteurService.showRecepteur());
        return "personne/recepteur";
    }

    @GetMapping("/recepteur/info")
    public String showRecepteurInfo(Model model){
        model.addAttribute("recepteurs", recepteurService.showRecepteur());
        return "personne/recepteurInfo";
    }
    @GetMapping("/choix/etat")
    public String afficherEtatClient(Model model){
        model.addAttribute("lesEtat", etatService.showStates());
        return "personne/choixEtatRecepteur";
    }

    @GetMapping("/choix/pays")
    public String afficherPaysClient(Model model){
        model.addAttribute("lesPays", paysService.showPays());
        return "personne/choixPaysRecepteur";
    }

    @GetMapping("/client/etat")
    public String afficherClientParEtat(@RequestParam("etatid") String etatid, Model model){
        model.addAttribute("lesEtat", etatService.showStates());
        model.addAttribute("recepteurs", recepteurService.clientSelonEtat(Long.parseLong(etatid)));
        model.addAttribute("nbreClient", recepteurService.clientSelonEtat(Long.parseLong(etatid)).size());
        model.addAttribute("etat", etatService.showOneState(Long.parseLong(etatid)));
        return "personne/choixEtatRecepteur";
    }

    @GetMapping("/client/etat/update")
    public String afficherClientParEtatUpdate(Long etatid, Model model){
        model.addAttribute("lesEtat", etatService.showStates());
        model.addAttribute("recepteurs", recepteurService.clientSelonEtat(etatid));
        model.addAttribute("nbreClient", recepteurService.clientSelonEtat(etatid).size());
        model.addAttribute("etat", etatService.showOneState(etatid));
        return "personne/choixEtatRecepteur";
    }

    @GetMapping("/client/pays")
    public String afficherClientParPays(@RequestParam("paysid") String paysid, Model model){
        model.addAttribute("lesPays", paysService.showPays());
        model.addAttribute("recepteurs", recepteurService.clientSelonPays(Long.parseLong(paysid)));
        model.addAttribute("pays", paysService.showOnePays(Long.parseLong(paysid)));
        model.addAttribute("nbreClient", recepteurService.clientSelonPays(Long.parseLong(paysid)).size());
        return "personne/choixPaysRecepteur";
    }

    @GetMapping("/client/pays/update")
    public String afficherClientParPaysStat(Long paysid, Model model){
        model.addAttribute("lesPays", paysService.showPays());
        model.addAttribute("recepteurs", recepteurService.clientSelonPays(paysid));
        model.addAttribute("pays", paysService.showOnePays(paysid));
        model.addAttribute("nbreClient", recepteurService.clientSelonPays(paysid).size());
        return "personne/choixPaysRecepteur";
    }

    @GetMapping("/recepteur/info/form/{id}")
    public String formInfo(@PathVariable("id") Long id, Model model){
        model.addAttribute("unRecepteur", recepteurService.showOneRecepteur(id));
        return "personne/formRecepteurInfo";
    }

    @GetMapping("/recepteur/info/debloquer/{id}")
    public String debloquerEmetteur(@PathVariable("id") Long id){
        recepteurService.updateInfoRecepteur(Constante.CLIENT_AUTORISE, id);
        return "redirect:/personne/recepteur/info";
    }

    //Renvoie la liste des clients recepteurs pour l'envoi
    @GetMapping("/recepteur/liste/{id}")
    public String showRecepteurListe(@PathVariable("id") Long id, Model model){
        model.addAttribute("UnEmetteur", emetteurService.showOneEmetteur(id));
        model.addAttribute("recepteurs", recepteurService.showRecepteur());
        return "personne/recepteurEnvoi";
    }

    //Renvoie le formulaire du destinataire
    @GetMapping("/recepteur/formRecepteur")
    public String showFormRecepteur(Model model){
        //model.addAttribute("etats", etatService.showStates());
        model.addAttribute("pays", paysService.showPays());
        return "personne/formAddRecepteur";
    }

    @GetMapping("/recepteur/formRecepteur2/{p}")
    public String showFormRecepteur2(@PathVariable("p") String p, Model model){
        //model.addAttribute("etats", etatService.showStates());
        model.addAttribute("pays", paysService.showPays());
        model.addAttribute("unEmetteur", emetteurService.showOneEmetteur(Long.parseLong(p)));
        return "personne/formAddRecepteur2";
    }

    @GetMapping("/erreur/codePostal")
    public String warningCodePostal(){
        return "personne/erreurCodePostal";
    }

    @GetMapping("/erreur/codePostal2")
    public String warningCodePostal2(long idE, Model model){
        model.addAttribute("unEmetteur", emetteurService.showOneEmetteur(idE));
        return "personne/erreurCodePostal2";
    }

    //Enregistrement du recpteur
    @PostMapping("/recepteur/nouveau")
    public String enregistrerRecepteur(Recepteur recepteur){
        List<Recepteur> listeRecepteur = new ArrayList<>();
        String numero = recepteur.getNomPersonne().substring(0,1)+""+recepteur.getPrenomPersonne().substring(0,1)+""+commandeService.genererNbre(Constante.BORNE_INF_CLIENT, Constante.BORNE_SUP_CLIENT);
        listeRecepteur = recepteurService.showRecepteur();
        while(recepteurService.testerAppartenance(listeRecepteur, numero)==true){
            numero = recepteur.getNumeroPersonne().substring(0,1)+""+recepteur.getPrenomPersonne().substring(0,1)+""+commandeService.genererNbre(Constante.BORNE_INF_CLIENT, Constante.BORNE_SUP_CLIENT);
        }
        recepteur.setNumeroPersonne(numero);
        /*if(recepteur.getEtatid()==null){
            recepteur.setEtatid(17L);
        }*/
        //System.out.println("*******************************PAYS = "+paysService.showOnePays(recepteur.getPaysid()).getLibellePays());
        if(paysService.showOnePays(recepteur.getPaysid()).getLibellePays().equals("Allemagne")){
            try {
                Codepostal codepostal = codepostalService.findEtatCodePostal(recepteur.getCodePostal());
                recepteur.setEtatid(codepostal.getEtatcode());
            }catch (NullPointerException e){
                return "redirect:/personne/erreur/codePostal";
            }
        }else {
            recepteur.setEtatid(17L);
        }

        recepteur.setSolde(0);
        recepteur.setUserid(Constante.showUserConnecte().getId());
        recepteur.setEtatPersonne(Constante.CLIENT_AUTORISE);
        recepteurService.saveRecepteur(recepteur);
        //return "redirect:/commande/formCommande";
        return "redirect:/personne/recepteurs";
    }

    @PostMapping("/recepteur/nouveau2")
    public String enregistrerRecepteur2(Long p, Recepteur recepteur){
        List<Recepteur> listeRecepteur = new ArrayList<>();
        String numero = recepteur.getNomPersonne().substring(0,1)+""+recepteur.getPrenomPersonne().substring(0,1)+""+commandeService.genererNbre(Constante.BORNE_INF_CLIENT, Constante.BORNE_SUP_CLIENT);
        listeRecepteur = recepteurService.showRecepteur();
        while(recepteurService.testerAppartenance(listeRecepteur, numero)==true){
            numero = recepteur.getNumeroPersonne().substring(0,1)+""+recepteur.getPrenomPersonne().substring(0,1)+""+commandeService.genererNbre(Constante.BORNE_INF_CLIENT, Constante.BORNE_SUP_CLIENT);
        }
        recepteur.setNumeroPersonne(numero);
        /*if(recepteur.getEtatid()==null){
            recepteur.setEtatid(17L);
        }*/
        if(paysService.showOnePays(recepteur.getPaysid()).getLibellePays().equals("Allemagne")){
            try {
                Codepostal codepostal = codepostalService.findEtatCodePostal(recepteur.getCodePostal());
                recepteur.setEtatid(codepostal.getEtatcode());
            }catch (NullPointerException e){
                return "redirect:/personne/erreur/codePostal2/?idE="+p;
            }
        }else {
            recepteur.setEtatid(17L);
        }
        recepteur.setSolde(0);
        recepteur.setUserid(Constante.showUserConnecte().getId());
        recepteur.setEtatPersonne(Constante.CLIENT_AUTORISE);
        recepteurService.saveRecepteur(recepteur);

        return "redirect:/commande/formCommande/?idE="+p+"&idR="+recepteurService.showMonDernierRecepteur(Constante.showUserConnecte().getId()).getId();
        //return "redirect:/personne/recepteurs";
    }

    //renvoie le formulaire de mise à jour
    @GetMapping("recepteur/formUpdate/{id}")
    public String showFormUpdateRecepteur(@PathVariable("id") Long id, Model model){
        model.addAttribute("unRecepteur", recepteurService.showOneRecepteur(id));
        model.addAttribute("pays", paysService.showPays());
        model.addAttribute("etats",etatService.showStates());
        return "personne/formUpdateRecepteur";
    }

    @GetMapping("recepteur/formUpdateStat/{id}")
    public String showFormUpdateRecepteurStat(@PathVariable("id") Long id, Model model){
        model.addAttribute("unRecepteur", recepteurService.showOneRecepteur(id));
        model.addAttribute("pays", paysService.showPays());
        model.addAttribute("etats",etatService.showStates());
        return "personne/formUpdateRecepteurStat";
    }

    @GetMapping("recepteur/formUpdateStatPays/{id}")
    public String showFormUpdateRecepteurStatPays(@PathVariable("id") Long id, Model model){
        model.addAttribute("unRecepteur", recepteurService.showOneRecepteur(id));
        model.addAttribute("pays", paysService.showPays());
        model.addAttribute("etats",etatService.showStates());
        return "personne/formUpdateRecepteurPays";
    }

    //Mettre à jour un recepteur
    @PostMapping("/recepteur/update")
    public String updateRecepteur(@ModelAttribute("recepteur") Recepteur recepteur){
        recepteurService.saveRecepteur(recepteur);
        return "redirect:/personne/recepteurs";
    }

    @PostMapping("/recepteurStat/update")
    public String updateRecepteurStat(@ModelAttribute("recepteur") Recepteur recepteur){
        recepteurService.saveRecepteur(recepteur);
        return "redirect:/personne/client/etat/update/?etatid="+recepteur.getEtatid();
    }

    @PostMapping("/recepteurStatPays/update")
    public String updateRecepteurPays(@ModelAttribute("recepteur") Recepteur recepteur){
        recepteurService.saveRecepteur(recepteur);
        return "redirect:/personne/client/pays/update/?paysid="+recepteur.getPaysid();
    }

}
