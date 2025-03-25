package org.tycoon.model.maingame;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tycoon.model.maingame.entity.Game;
import org.tycoon.model.maingame.entity.Product;
import org.tycoon.model.maingame.entity.Revenue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MainGameServiceImpl {
  private final MainGameRepository mainGameRepository;

  @Override
  @Transactional
  public int startNewGame(Game game){
    int result = mainGameRepository.newGame(game);
    int gameCode = mainGameRepository.getNewGameCode(game.getUserId());
    int result2 = mainGameRepository.newLoan(game.getUserId(), gameCode);
    if(result>0 && result2>0){
      return gameCode;
    }else{
      return 0;
    }
  }

  @Override
  public List<Product> getAllProductByGameNo(Integer gameNo){
    List<Product> product = mainGameRepository.getAllProductByGameNo(gameNo);
    return product;
  }

  @Override
  @Transactional
  public int saveRevenueAndUpdateStorage(Revenue revenue, List<Storage> storage){
    int result = mainGameRepository.saveEndResult(revenue);
    int result2 = mainGameRepository.moneyChange(revenue.getDayendcash(),revenue.getPlayNo());
    int result3 = mainGameRepository.daysChange(revenue.getSalesDay(), revenue.getPlayNo());
    storage.forEach(s->{
      mainGameRepository.updateStorage(s);
    });

    return result;
  }

  @Override
  public int moneyExpense(Integer price, Integer gameNo){
    return mainGameRepository.moneyExpense(price, gameNo);
  }

  @Override
  public int getMoneyData(Integer gameNo){
    return mainGameRepository.getMoneyData(gameNo);
  }

  @Override
  @Transactional
  public Map<String,Object> getCashLoanQuiz(Integer gameNo){
    Map<String,Object> data = new HashMap<>();

    // playday, cash, loan
    Game cashAndLoan = mainGameRepository.getCashAndLoan(gameNo);
    // quizYN, disposal
    List<Revenue> revenueList = mainGameRepository.getQuizAndDispose(gameNo);

    data.put("cashloan",cashAndLoan);
    data.put("quizdisposal",revenueList);

    return data;
  }
//  @Override
//  public int saveEndResult(Revenue revenue){
//    return mainGameRepository.saveEndResult(revenue);
//  }
//
//  @Override
//  public int updateStorage(Storage storage){ return mainGameRepository.updateStorage(storage); }


  @Override
  public int getGamePlayDay(String userId) {
    return mainGameRepository.getGamePlayDay(userId);
  }
}
