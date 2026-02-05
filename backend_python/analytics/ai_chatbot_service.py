def ai_portfolio_chat(question, holdings=None):
    question = question.lower().strip()

    predefined_answers = {
        "is my portfolio suitable for long-term investing?":
            "Yes, your portfolio is suitable for long-term investing as it contains stable large-cap stocks. "
            "For better long-term growth, you may consider adding ETFs or sector diversification.",

        "how diversified is my portfolio?":
            "Your portfolio shows moderate diversification. Most investments are concentrated in a few sectors. "
            "Adding stocks from FMCG, Pharma, or Index ETFs can improve diversification.",

        "what is the risk level of my portfolio?":
            "Your portfolio has a medium risk level. Large-cap stocks reduce volatility, but sector concentration "
            "can increase risk during market downturns.",

        "should i invest more in this portfolio?":
            "You may invest more if your goal is long-term wealth creation. However, consider spreading new investments "
            "across different sectors to reduce risk.",

        "suggest a portfolio name":
            "Balanced Growth Portfolio",

        "how can i reduce my portfolio risk?":
            "You can reduce risk by investing in index funds, diversifying across sectors, and avoiding overexposure "
            "to a single stock.",

        "what happens if the market crashes?":
            "In case of a market crash, diversified portfolios tend to recover faster. Holding quality stocks "
            "and avoiding panic selling is usually recommended."
    }

    
    for key in predefined_answers:
        if key in question:
            return {
                "question": question,
                "answer": predefined_answers[key],
                "mode": "predefined"
            }

 
    return {
        "question": question,
        "answer": "This is a demo chatbot. Please select one of the predefined questions for analysis.",
        "mode": "fallback"
    }