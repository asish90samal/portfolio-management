package com.asish.portfolio_investment.Service;



import com.asish.portfolio_investment.Entity.Holding;
import com.asish.portfolio_investment.Entity.Portfolio;
import com.asish.portfolio_investment.Entity.Trade;
import com.asish.portfolio_investment.Repository.HoldingRepository;
import com.asish.portfolio_investment.Repository.PortfolioRepository;
import com.asish.portfolio_investment.Repository.TradeRepository;
import com.asish.portfolio_investment.dto.MarketPriceResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TradeService {

    @Autowired
    private PortfolioRepository portfolioRepository;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private HoldingRepository holdingRepository;

    @Autowired
    private MarketDataService marketDataService;

    @Transactional
    public Trade buyAsset(Long portfolioId, String symbol, int quantity, Double manualPrice) {

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        double price;

        if (manualPrice != null && manualPrice > 0) {
            price = manualPrice; // MANUAL MODE
        } else {
            price = marketDataService.getLivePrice(symbol).getPrice(); // LIVE MODE
        }

        double totalCost = price * quantity;

        if (portfolio.getCashBalance() < totalCost) {
            throw new RuntimeException("Insufficient balance");
        }

        portfolio.setCashBalance(portfolio.getCashBalance() - totalCost);

        Trade trade = new Trade();
        trade.setSymbol(symbol);
        trade.setQuantity(quantity);
        trade.setPrice(price);
        trade.setType("BUY");
        trade.setPortfolio(portfolio);

        tradeRepository.save(trade);

        Holding holding = holdingRepository
                .findByPortfolioAndSymbol(portfolio, symbol)
                .orElse(new Holding());

        holding.setPortfolio(portfolio);
        holding.setSymbol(symbol);
        holding.setQuantity(holding.getQuantity() + quantity);
        holding.setAveragePrice(price);

        holdingRepository.save(holding);
        portfolioRepository.save(portfolio);

        return trade;
    }


    @Transactional
    public Trade sellAsset(Long portfolioId, String symbol, int quantity, Double manualPrice) {

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        Holding holding = holdingRepository
                .findByPortfolioAndSymbol(portfolio, symbol)
                .orElseThrow(() -> new RuntimeException("No holdings found"));

        if (holding.getQuantity() < quantity) {
            throw new RuntimeException("Not enough quantity to sell");
        }

        double price;

        if (manualPrice != null && manualPrice > 0) {
            price = manualPrice;
        } else {
            price = marketDataService.getLivePrice(symbol).getPrice();
        }

        double proceeds = price * quantity;

        holding.setQuantity(holding.getQuantity() - quantity);
        portfolio.setCashBalance(portfolio.getCashBalance() + proceeds);

        Trade trade = new Trade();
        trade.setSymbol(symbol);
        trade.setQuantity(quantity);
        trade.setPrice(price);
        trade.setType("SELL");
        trade.setPortfolio(portfolio);

        tradeRepository.save(trade);
        holdingRepository.save(holding);
        portfolioRepository.save(portfolio);

        return trade;
    }
    public List<Trade> getTradesByPortfolio(Long portfolioId) {

        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));

        return tradeRepository.findByPortfolio(portfolio);
    }


}

