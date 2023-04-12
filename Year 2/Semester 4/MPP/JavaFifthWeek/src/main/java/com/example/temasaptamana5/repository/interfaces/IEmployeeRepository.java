package com.example.temasaptamana5.repository.interfaces;

import com.example.temasaptamana5.domain.Entity;
import com.example.temasaptamana5.repository.IRepository;

public interface IEmployeeRepository <ID, E extends Entity<ID>> extends IRepository<ID, E> {
}
