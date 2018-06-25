package com.jt.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.cart.mapper.CartMapper;
import com.jt.cart.pojo.Cart;
import com.jt.common.service.BaseService;

@Service
public class CartServiceImpl extends BaseService<Cart> implements CartService {
	
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartByUserId(Long userId) {
		Cart cart = new Cart();
		cart.setUserId(userId);
	
		return cartMapper.select(cart);
	}
	
	/**
	 * 1.根据itemId和userId查询购物车信息
	 * 2.如果查询的结果不为null,则应该做购物车的更新操作
	 * 3.如果查询的结果为null,入库购物车
	 */
	@Override
	public void saveCart(Cart cart) {
		Cart cartTemp = new Cart();
		cartTemp.setUserId(cart.getUserId());
		cartTemp.setItemId(cart.getItemId());
		//如果数据不为null,则充当where条件 
		Cart cartDB = super.queryByWhere(cartTemp);
		
		if(cartDB == null){
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else{
			//求和购物数量
			int num = cartDB.getNum() + cart.getNum();
			cartDB.setNum(num);
			cartMapper.updateByPrimaryKeySelective(cartDB);
		}
	}

	@Override
	public void updateCartNum(Cart cart) {
		
		cart.setUpdated(new Date());
		cartMapper.updateCartNum(cart);
	}

	@Override
	public void deleteCart(Long userId, Long itemId) {
		Cart cart = new Cart();
		cart.setUserId(userId);
		cart.setItemId(itemId);
		super.deleteByWhere(cart);
	}
	
	
	
	
	
	
}
