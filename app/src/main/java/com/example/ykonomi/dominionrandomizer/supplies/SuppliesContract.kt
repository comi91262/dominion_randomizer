import com.example.ykonomi.dominionrandomizer.BasePresenter
import com.example.ykonomi.dominionrandomizer.utils.BaseView

interface SuppliesContract {

    interface View : BaseView<Presenter> {
        fun showSwitch()
    }

    interface Presenter : BasePresenter {
        fun getData()
    }

}
