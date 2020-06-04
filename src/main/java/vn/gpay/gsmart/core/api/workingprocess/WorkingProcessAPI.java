package vn.gpay.gsmart.core.api.workingprocess;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.workingprocess.IWorkingProcess_Service;
import vn.gpay.gsmart.core.workingprocess.WorkingProcess;
@RestController
@RequestMapping("/api/v1/workingprocess")
public class WorkingProcessAPI {
    @Autowired
    private IWorkingProcess_Service workingprocessRepository;

    
    @GetMapping("/workingprocess")
    public List<WorkingProcess> getAllProcess() {
        return workingprocessRepository.findAll();
    }

    @GetMapping("/workingprocess/{id}")
    public ResponseEntity<WorkingProcess> getProcessById(@PathVariable(value = "id") Long processId){
        WorkingProcess process = workingprocessRepository.findOne(processId);
        return ResponseEntity.ok().body(process);
    }
    
    @PostMapping("/workingprocess")
    public WorkingProcess createProcess(@Valid @RequestBody WorkingProcess process) {
        return workingprocessRepository.save(process);
    }

    @PutMapping("/workingprocess/{id}")
    public ResponseEntity<WorkingProcess> updateProcess(@PathVariable(value = "id") Long orgId,
         @Valid @RequestBody WorkingProcess processDetails){
        WorkingProcess process = workingprocessRepository.findOne(orgId);

        process.setName(processDetails.getName());
        process.setParentid_link(processDetails.getParentid_link());
        final WorkingProcess updatedProcess = workingprocessRepository.save(process);
        return ResponseEntity.ok(updatedProcess);
    }

    @DeleteMapping("/workingprocess/{id}")
    public Map<String, Boolean> deleteProcess(@PathVariable(value = "id") Long orgId){
        WorkingProcess process = workingprocessRepository.findOne(orgId);

        workingprocessRepository.delete(process);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }	
}
