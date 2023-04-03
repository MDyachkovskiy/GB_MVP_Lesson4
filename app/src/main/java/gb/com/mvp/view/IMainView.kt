package gb.com.mvp.view

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType (AddToEndSingleStrategy::class)
interface IMainView: MvpView {
}