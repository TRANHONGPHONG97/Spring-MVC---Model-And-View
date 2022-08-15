package com.cg.controller;


import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.service.CustomerService;
import com.cg.service.DepositService;
import com.cg.service.TransferService;
import com.cg.service.WithDrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private DepositService depositService;

    @Autowired
    private TransferService transferService;
    @Autowired
    private WithDrawService withDrawService;


    @GetMapping
    public ModelAndView showListPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/index");

        List<Customer> customerList = customerService.findAll();

        modelAndView.addObject("customers", customerList);
        return modelAndView;
    }

//    @GetMapping("/listInfo")
//    public ModelAndView showListInfo() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("transfer/list");
//
//        List<Customer> customerList = customerService.findAll();
//
//        modelAndView.addObject("customers", customerList);
//        return modelAndView;
//    }

    @GetMapping("/create")
    public ModelAndView showCreatePage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/create");

//        Customer customer = new Customer();
        modelAndView.addObject("customer", new Customer());

        return modelAndView;
    }

    @PostMapping("/create")
    public ModelAndView doCreate(@Validated  @ModelAttribute Customer customer, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/create");

        if (bindingResult.hasFieldErrors()){
            modelAndView.addObject("errors", true);
//            modelAndView.addObject("customer", customer);
            return modelAndView;
        }
        customer.setId(0L);
        customer.setBalance(new BigDecimal(0L));
        customerService.save(customer);

//        modelAndView.addObject("message", "Đã thêm thành công");
        modelAndView.addObject("customer", new Customer());

        return modelAndView;

    }

    //Put cập nhập tất cả
    //Pacth cập nhật view nào đưa lên thôi, thì nó cập nhật

    @PostMapping("/update/{id}")
    public ModelAndView doUpdate(@PathVariable long id, @ModelAttribute Customer customer) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/edit");
        Optional<Customer> customerOptional = customerService.findById(id);

        if (!customerOptional.isPresent()) {
            modelAndView.setViewName("/error.404");
            return modelAndView;
        }
        //undatable = false : khi cập nhật customer thì trường balace không bị mất
        customer.setId(id);
        customer.setBalance(customerOptional.get().getBalance());

        customerService.save(customer);

        modelAndView.addObject("customer", customer);
        modelAndView.addObject("message", "Đã sửa thành công");

        return modelAndView;
    }


    @GetMapping("/update/{id}")
    public ModelAndView showCustomerByIdPage(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/edit");

//        Customer customer = hibernateCustomerService.findById(id);

        Optional<Customer> customer = customerService.findById(id);

        if (!customer.isPresent()) {
            modelAndView.setViewName("/error.404");
            return modelAndView;
        }

        modelAndView.addObject("customer", customer.get());

        return modelAndView;
    }

    @GetMapping("/delete-customer/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Optional<Customer> customer = customerService.findById(id);
        if (customer.isPresent()) {
            ModelAndView modelAndView = new ModelAndView("customer/suspended");
            modelAndView.addObject("customer", customer.get());
            return modelAndView;

        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/delete-customer")
    public ModelAndView deleteCustomer(@ModelAttribute("customer") Customer customer) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/suspended");
        customerService.remove(customer.getId());
        modelAndView.addObject("message", "Đã xóa thành công");
        return modelAndView;
    }

    @GetMapping("/deposit/{customerId}")
    public ModelAndView showDepositPage(@PathVariable Long customerId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/deposit");

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (!customerOptional.isPresent()) {
            modelAndView.setViewName("error.404");
            return modelAndView;
        }

        modelAndView.addObject("customer", customerOptional.get());
        modelAndView.addObject("deposit", new Deposit());


        return modelAndView;
    }

    @GetMapping("/withdraw/{id}")
    private ModelAndView viewWithdraw(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/widthraw");

        Optional<Customer> customer = customerService.findById(id);
        if (!customer.isPresent()) {
            modelAndView.setViewName("error.404");

            return modelAndView;


        } else {
            modelAndView.addObject("withdraw", new Withdraw());
            modelAndView.addObject("customer", customer.get());

            modelAndView.addObject("success", null);
            modelAndView.addObject("error", null);
            return modelAndView;
        }
    }

    @GetMapping("/transfer/{senderId}")
    public ModelAndView showTransferPage(@PathVariable Long senderId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/transfer");

        Optional<Customer> senderOptional = customerService.findById(senderId);

        if (!senderOptional.isPresent()) {
            modelAndView.setViewName("error.404");
            return modelAndView;
        }

        List<Customer> recipients = customerService.findByIdIsNot(senderId);

        modelAndView.addObject("sender", senderOptional.get());
        modelAndView.addObject("recipients", recipients);
        modelAndView.addObject("transfer", new Transfer());

        return modelAndView;
    }

    @PostMapping("/deposit/{customerId}")
    public ModelAndView doCreate(@PathVariable Long customerId,@ModelAttribute Customer customer,
                                 @ModelAttribute Deposit deposit, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/deposit");
        if (bindingResult.hasFieldErrors()) {
            modelAndView.addObject("errors", true);
//            modelAndView.addObject("deposit", deposit);
            return modelAndView;
        }
        Optional<Customer> customerOptional = customerService.findById(customerId);
        if (!customerOptional.isPresent()) {
            modelAndView.setViewName("error.404");
            return  modelAndView;
        }
//        Customer customer = customerOptional.get();
//        BigDecimal currentBalance = customer.getBalance();
//        BigDecimal newBalance;
//        BigDecimal transactionAmount = deposit.getTransactionAmount();
//        newBalance = currentBalance.add(transactionAmount);

        deposit.setCustomer(customerOptional.get());
        Customer newCustomer = depositService.deposit(deposit);
//        customer.setBalance(newBalance);

//        deposit.setCustomer(customer);

//2 thằng này tạo 2 đối tượng
//        depositService.save(deposit);
//        Customer newCustomer = customerService.save(customer);

        modelAndView.addObject("customer", newCustomer);
        modelAndView.addObject("deposit", new Deposit());

        return modelAndView;
    }

    @PostMapping("/withdraw/{id}")
    public ModelAndView doWithdraw(@PathVariable Long id, @ModelAttribute Customer customer,
                                   @ModelAttribute Withdraw withdraw, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/widthraw");

        if (bindingResult.hasErrors()) {
            modelAndView.addObject("errors", true);
//            modelAndView.addObject("deposit", deposit);
            return modelAndView;
        }
        Optional<Customer> customerOptional = customerService.findById(id);
        if (!customerOptional.isPresent()) {
            modelAndView.setViewName("error.404");
            return  modelAndView;
        }
        customer = customerOptional.get();
        BigDecimal currentBalance = customer.getBalance();
        BigDecimal newBalance;
        BigDecimal transactionAmount = withdraw.getTransactionAmount();
        newBalance = currentBalance.add(transactionAmount);

//        withdraw.setCustomer(customerOptional.get());
//        Customer newCustomer = withDrawService.withdraw(withdraw);
        customer.setBalance(newBalance);

        withdraw.setCustomer(customer);

//2 thằng này tạo 2 đối tượng
        withDrawService.save(withdraw);
        Customer newCustomer = customerService.save(customer);

        modelAndView.addObject("customer", newCustomer);
        modelAndView.addObject("withdraw", new Withdraw());

        return modelAndView;
    }

    @PostMapping("/transfer/{senderId}")
    public ModelAndView doTransfer(@PathVariable Long senderId, @ModelAttribute Transfer transfer) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/transfer");

        Optional<Customer> senderOptional = customerService.findById(senderId);
        Customer sender = senderOptional.get();

        BigDecimal currentSenderBalance = sender.getBalance();

        BigDecimal transferAmount = transfer.getTransferAmount();
        BigDecimal fees = new BigDecimal(10L);
        BigDecimal feesAmount = transferAmount.multiply(fees).divide(new BigDecimal(100L));
        BigDecimal transactionAmount = transferAmount.add(feesAmount);

        if (transactionAmount.compareTo(currentSenderBalance) > 0) {
            modelAndView.addObject("errors", "Số dư tài khoản không đủ thực hiện giao dịch");
        } else {
            transfer.setFees(fees.longValueExact());
            transfer.setFeesAmount(feesAmount);
            transfer.setTransactionAmount(transactionAmount);
            transfer.setSender(senderOptional.get());

            transfer.setSender(sender);

//            transferService.save(transfer);
//
//            currentSenderBalance = currentSenderBalance.subtract(transactionAmount);
//            sender.setBalance(currentSenderBalance);
//
//            sender = customerService.save(sender);
//
//            Customer recipient = transfer.getRecipient();
//            BigDecimal newRecipientBalance = recipient.getBalance();
//            newRecipientBalance = newRecipientBalance.add(transferAmount);
//            recipient.setBalance(newRecipientBalance);
//
//            customerService.save(recipient);

            transferService.doTransfer(transfer);
        }

        List<Customer> recipients = customerService.findByIdIsNot(senderId);

        modelAndView.addObject("sender", sender);
        modelAndView.addObject("recipients", recipients);
        modelAndView.addObject("transfer", new Transfer());


        return modelAndView;
    }


}