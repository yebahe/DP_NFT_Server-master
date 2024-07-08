package cn.dp.nft.base.statemachine;

/**
 * @author yebahe
 */
public interface StateMachine<STATE, EVENT> {

    /**
     * 状态机转移
     *
     * @param state
     * @param event
     * @return
     */
    public STATE transition(STATE state, EVENT event);
}

