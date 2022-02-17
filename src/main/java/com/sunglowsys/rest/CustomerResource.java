package com.sunglowsys.rest;

import com.sunglowsys.domain.Customer;
import com.sunglowsys.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;

@Controller
@RequestMapping("/")
public class CustomerResource {
    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);
    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }
    @GetMapping
    public String home(Pageable pageable , Model model) throws URISyntaxException{
        log.debug("REST request to get Customer : {}" ,pageable);
        Page<Customer>  page=customerService.findAll(pageable);
        List<Customer> customers = page.getContent();
         model.addAttribute("customers" , customers);
         return "index";
    }

    @GetMapping("/customers/create")
    public String createCustomerForm(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer" , customer);
        return "add-customer";
    }
    @PostMapping("/customers")
    public String createCustomer(@ModelAttribute("customer") Customer customer) throws URISyntaxException{
        log.debug("Rest request to create customer : {}" ,customer);
        if (customer.getId() == null) {
            customerService.save(customer);
        }
        else {
            customerService.update(customer);
        }
        return "redirect:/";
    }
    @GetMapping("customers/{id}")
    public String updateForm(@PathVariable Long id , Model model) {
        Customer customer = customerService.findById(id).get();
        model.addAttribute("customer" , customer);
        return "add-customer";
    }
    @GetMapping("/customers/delete/{id}")
    public  String deleteCustomer(@PathVariable Long id) {
        log.debug("Rest request to delete customer : {}" ,id);
        customerService.delete(id);
        return "redirect:/";
    }
}
