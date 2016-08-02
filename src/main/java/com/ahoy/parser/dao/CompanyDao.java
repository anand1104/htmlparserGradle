package com.ahoy.parser.dao;

import com.ahoy.parser.domain.CompanyDo;

import java.util.List;

public interface CompanyDao {
	public List<CompanyDo> selectAll();
}
