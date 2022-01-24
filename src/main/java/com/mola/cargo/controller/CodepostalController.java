package com.mola.cargo.controller;

import com.mola.cargo.model.Codepostal;
import com.mola.cargo.service.CodepostalService;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class CodepostalController {

    @Autowired
    private CodepostalService codepostalService;

    @GetMapping("/codepostals")
    public String afficherCodepostal(Model model){
        model.addAttribute("codepostals",codepostalService.showCodepostal());
        return "pays/codepostal";
    }
    @PostMapping("/codepostal/nouveau")
    public String enregistrerCodepostal() throws IOException {
        List<Codepostal> touslescodes = new ArrayList<>();
        FileInputStream inputStream = new FileInputStream(new File("D:/codef2.xlsx"));
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = sheet.iterator();
        while(rowIterator.hasNext()){
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            Codepostal codepostal = new Codepostal();
            while (cellIterator.hasNext()){
                //Cell cell = cellIterator.next();
                codepostal.setLibelleCode(((int)cellIterator.next().getNumericCellValue()));
                codepostal.setEtatcode(((long)cellIterator.next().getNumericCellValue()));
            }
            touslescodes.add(codepostal);
        }
        codepostalService.saveCodepostal(touslescodes);
        return "redirect:/codepostals";
    }
}
