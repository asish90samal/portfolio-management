package com.asish.portfolio_investment.Controller;



import com.asish.portfolio_investment.Entity.Trade;
import com.asish.portfolio_investment.Service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trades")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/buy")
    public Trade buyAsset(
            @RequestParam Long portfolioId,
            @RequestParam String symbol,
            @RequestParam int quantity,
            @RequestParam(required = false) Double price
    ) {
        return tradeService.buyAsset(portfolioId, symbol, quantity, price);
    }
    @PostMapping("/sell")
    public Trade sellAsset(
            @RequestParam Long portfolioId,
            @RequestParam String symbol,
            @RequestParam int quantity,
            @RequestParam(required = false) Double price
    ) {
        return tradeService.sellAsset(portfolioId, symbol, quantity, price);
    }
    @GetMapping("/portfolio/{portfolioId}")
    public List<Trade> getTrades(@PathVariable Long portfolioId) {
        return tradeService.getTradesByPortfolio(portfolioId);
    }


}