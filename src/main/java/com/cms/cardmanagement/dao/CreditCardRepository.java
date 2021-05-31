package com.cms.cardmanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cms.cardmanagement.orm.CreditCard;

public interface CreditCardRepository extends JpaRepository<CreditCard,Long>{

}
