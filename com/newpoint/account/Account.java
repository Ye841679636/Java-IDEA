package com.newpoint.account;

import com.newpoint.instrument.Security;

import java.util.*;

public class Account {
    private final String accountKey;

    //Net liquidation Value = Total cash value + stock value + securities options value + bond value + fund value.
    private double netValue;
    //Settled cash + sales at the time of trade + futures P&L
    private double cashValue;
    //Cash recognized at the time of settlement - purchases at the time of trade - commissions - taxes - fees.
    //Stock Settlement: Trade date + 2 days, Options Settlement: Trade date + 1 day
    private double settledCash;

    private double accruedCash;
    //Buying power serves as a measurement of the dollar value of securities that one may purchase
    // in a securities account without depositing additional funds
    private double buyingPower;
    //PreviousEquityWithLoanValue
    private double previousDayBal;

    private double initMarginReq;
    private double maintenanceMarginReq;
    private double availableFund;
    private double fullInitMarginReq;
    private double fullMaintMarginReq;
    private double fullAvailableFunds;

    private Date updateTime;
    //positions for the account
    private final Set<Position> positions;

    public static final Set<String> attributeSet = new HashSet<String>();
    static {
        for (AccountAttribute attr: AccountAttribute.values())
        {
            attributeSet.add(attr.toString());
        }
    }

    public Account(String acctKey) {
        this.accountKey = acctKey;
        this.positions = new HashSet<Position>();
    }


    public String toString() {
        final StringBuilder sb = new StringBuilder("Account{");
        sb.append("accountKey='").append(accountKey).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return accountKey.equals(account.accountKey);
    }

    public int hashCode() {
        return Objects.hash(accountKey);
    }

    public double getNetValue() {
        return netValue;
    }

    public void setNetValue(double netValue) {
        this.netValue = netValue;
    }

    public double getCashValue() {
        return cashValue;
    }

    public double getInitMarginReq() {
        return initMarginReq;
    }

    public void setInitMarginReq(double initMarginReq) {
        this.initMarginReq = initMarginReq;
    }

    public double getMaintenanceMarginReq() {
        return maintenanceMarginReq;
    }

    public void setMaintenanceMarginReq(double maintenanceMarginReq) {
        this.maintenanceMarginReq = maintenanceMarginReq;
    }

    public double getFullMaintMarginReq() {
        return fullMaintMarginReq;
    }

    public void setFullMaintMarginReq(double fullMaintMarginReq) {
        this.fullMaintMarginReq = fullMaintMarginReq;
    }

    public double getAvailableFund() {
        return availableFund;
    }

    public double getFullInitMarginReq() {
        return fullInitMarginReq;
    }

    public double getFullAvailableFunds() {
        return fullAvailableFunds;
    }

    public void setFullAvailableFunds(double fullAvailableFunds) {
        this.fullAvailableFunds = fullAvailableFunds;
    }

    public void setFullInitMarginReq(double fullInitMarginReq) {
        this.fullInitMarginReq = fullInitMarginReq;
    }

    public void setAvailableFund(double availableFund) {
        this.availableFund = availableFund;
    }

    public void setCashValue(double cashValue) {
        this.cashValue = cashValue;
    }

    public double getPreviousDayBal() {
        return previousDayBal;
    }

    public double getAccruedCash() {
        return accruedCash;
    }

    public void setAccruedCash(double accruedCash) {
        this.accruedCash = accruedCash;
    }

    public void setPreviousDayBal(double previousDayBal) {
        this.previousDayBal = previousDayBal;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public double getSettledCash() {
        return settledCash;
    }

    public void setSettledCash(double settledCash) {
        this.settledCash = settledCash;
    }

    public double getBuyingPower() {
        return buyingPower;
    }

    public void setBuyingPower(double buyingPower) {
        this.buyingPower = buyingPower;
    }

    public Set<Position> getPositions() {
        return positions;
    }

    public void addPosition(Position pos) {
        positions.add(pos);
    }

    public void removePosition(Position pos) {
        positions.remove(pos);
    }

    public Position getPosition(Security security) {
        for(Position pos : positions ) {
            if (pos.getInstrument().equals(security))
                return pos;
        }
        return null;
    }

    public boolean containsAttribute(String attr) {
        return attributeSet.contains(attr);
    }

    public void setAttribute(String attribute, double value) {
        switch (AccountAttribute.valueOf(attribute)) {
            case NetLiquidation:
                setNetValue(value);
                break;
            case TotalCashValue:
                setCashValue(value);
                break;
            case SettledCash:
                setSettledCash(value);
                break;
            case AccruedCash:
                setAccruedCash(value);
                break;
            case BuyingPower:
                setBuyingPower(value);
            case PreviousEquityWithLoanValue:
                setPreviousDayBal(value);
            case FullInitMarginReq:
                setFullInitMarginReq(value);
                break;
            case FullMaintMarginReq:
                setFullMaintMarginReq(value);
                break;
            case InitMarginReq:
                setInitMarginReq(value);
                break;
            case MaintMarginReq:
                setMaintenanceMarginReq(value);
                break;
            case AvailableFunds:
                setAvailableFund(value);
                break;
            case FullAvailableFunds:
                setFullAvailableFunds(value);
                break;
            case EquityWithLoanValue:
            case GrossPositionValue:
            case ReqTEquity:
            case ReqTMargin:
            case SMA:
            case ExcessLiquidity:
            case Cushion:
            case FullExcessLiquidity:
            case LookAheadNextChange:
            case LookAheadInitMarginReq:
            case LookAheadMaintMarginReq:
            case LookAheadAvailableFunds:
            case LookAheadExcessLiquidity:
            case HighestSeverity:
            case DayTradesRemaining:
            case Leverage:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + AccountAttribute.valueOf(attribute));
        }
    }
}


