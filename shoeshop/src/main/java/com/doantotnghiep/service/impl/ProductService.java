package com.doantotnghiep.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.doantotnghiep.converter.ImageConverter;
import com.doantotnghiep.converter.ProductConverter;
import com.doantotnghiep.dto.DTOImage;
import com.doantotnghiep.dto.DTOProduct;
import com.doantotnghiep.entities.ImageEntity;
import com.doantotnghiep.entities.ProductEntity;
import com.doantotnghiep.repository.ProductRepository;
import com.doantotnghiep.service.IProductService;

@Service
public class ProductService implements IProductService{
	@Autowired
	private ProductRepository repository;
	@Autowired
	private ProductConverter converter;
	@Autowired
	private ImageService imageService;
	@Autowired
	private ImageConverter imageConverter;
	@Override
	public DTOProduct saveDTO(DTOProduct product,MultipartFile[]files) throws IOException {
		
		
		ProductEntity entity = new ProductEntity();
		
		Set<DTOImage>images = new HashSet<>();
		Set<ImageEntity> imageEntities= new HashSet<>();
		

		if(product.getId() != null) {
			ProductEntity oldEntity = repository.findOneById(product.getId());
			entity = converter.toEntity(product, oldEntity);
		}else {
			
			entity = converter.toEntity(product);
		}
		
		//add image cho product 
		for (MultipartFile file : files) {
			ImageEntity item = new ImageEntity();
			item.setProduct(entity);
			item.setName(file.getOriginalFilename());
			item.setData(file.getBytes());
			item.setType(file.getContentType());
			item.setDescription(imageConverter.toBase64(file.getBytes()));
			imageEntities.add(item);
		}
		entity.setImages(imageEntities);
		repository.save(entity);
		
		return converter.toDTO(entity);
	}
	@Override
	public List<DTOProduct> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		List<DTOProduct> products = new ArrayList<>();
		List<ProductEntity> productEntities = repository.findAll();
		for (ProductEntity productEntity : productEntities) {
			products.add(converter.toDTO(productEntity));
		}
		
		return products;
	}
	@Override
	public int totalItem() {
		// TODO Auto-generated method stub
		return (int) repository.count();
	}
	@Override
	public List<DTOProduct> findAll() {
		List<DTOProduct> products = new ArrayList<>();
		List<ProductEntity> productEntities = repository.findAll();
		for (ProductEntity productEntity : productEntities) {
			products.add(converter.toDTO(productEntity));
		}
		
		return products;
	}
	@Override
	public DTOProduct findById(Long id) {
		// TODO Auto-generated method stub
		return converter.toDTO(repository.findOneById(id));
	}

}
