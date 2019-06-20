package com.mayikt.pay.controller;

import com.mayikt.api.pay.service.PayMentTransacInfoService;
import com.mayikt.api.pay.service.PaymentChannelService;
import com.mayikt.common.base.BaseResponse;
import com.mayikt.pay.out.dto.PayMentTransacDTO;
import com.mayikt.pay.out.dto.PaymentChannelDTO;
import com.mayikt.web.base.BaseWebController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 */
@Controller
public class PayController extends BaseWebController {

	@Autowired
	private PayMentTransacInfoService payMentTransacInfoFeign;
	@Autowired
	private PaymentChannelService paymentChannelFeign;


	@RequestMapping("/pay")
	public String index(String payToken, Model model) {
		// 1.验证payToken参数
		if (StringUtils.isEmpty(payToken)) {
			setErrorMsg(model, "支付令牌不能为空!");
			return ERROR_500_FTL;
		}
		// 2.使用payToken查询支付信息
		BaseResponse<PayMentTransacDTO> tokenByPayMentTransac = payMentTransacInfoFeign.tokenByPayMentTransac(payToken);
		if (!isSuccess(tokenByPayMentTransac)) {
			setErrorMsg(model, tokenByPayMentTransac.getMsg());
			return ERROR_500_FTL;
		}
		// 3.查询支付信息
		PayMentTransacDTO data = tokenByPayMentTransac.getData();
		model.addAttribute("data", data);
		// 4.查询渠道信息
		List<PaymentChannelDTO> paymentChanneList = paymentChannelFeign.selectAll();
		model.addAttribute("paymentChanneList", paymentChanneList);
		return "index";
	}
}
