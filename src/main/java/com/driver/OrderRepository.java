package com.driver;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        // your code here
        orderMap.put(order.getId(),order);
    }

    public void savePartner(String partnerId){
        // your code here
        DeliveryPartner partner=new DeliveryPartner(partnerId);
        partnerMap.put(partnerId,partner);
        // create a new partner with given partnerId and save it
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)){
            // your code here
            //add order to given partner's order list
            orderToPartnerMap.put(orderId,partnerId);

            //increase order count of partner
            DeliveryPartner partner=findPartnerById(partnerId);
            partner.setNumberOfOrders(partner.getNumberOfOrders()+1);
            //assign partner to this order
            HashSet<String> orderlist=new HashSet<>();
            orderlist.add(orderId);
            partnerToOrderMap.put(partnerId,orderlist);

        }
    }

    public Order findOrderById(String orderId){
        // your code here
if(orderMap.containsKey(orderId)){
    return orderMap.get(orderId);
}
return null;

    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here
        if(partnerMap.containsKey(partnerId)){
            return partnerMap.get(partnerId);
        }
        return null;

    }

    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here

        DeliveryPartner partner=findPartnerById(partnerId);
        if(partner!=null){
            return partner.getNumberOfOrders();
        }
        return 0;

    }

    public List<String> findOrdersByPartnerId(String partnerId){
        // your code here
        if(partnerToOrderMap.containsKey(partnerId)){
            HashSet<String>orderlist=partnerToOrderMap.get(partnerId);
            List<String> orders=new ArrayList<>();
            for(String str:orderlist){
                orders.add(str);
            }
            return orders;
        }
        return new ArrayList<>();

    }

    public List<String> findAllOrders(){
        // your code here
        // return list of all orders
        List<String> orders=new ArrayList<>();
        for(String str:orderMap.keySet()){
            orders.add(str);
        }
        return orders;

    }

    public void deletePartner(String partnerId){
        // your code here
        // delete partner by ID
        if(partnerMap.containsKey(partnerId)){
            DeliveryPartner partner=partnerMap.get(partnerId);
            partnerMap.remove(partnerId);// partner removed
            if(partnerToOrderMap.containsKey(partnerId)){
                HashSet<String> orderlist=partnerToOrderMap.remove(partnerId);
                for(String str:orderlist){
                    orderToPartnerMap.remove(str);// unpairing all orders with this delivery partner
                }

            }
        }


    }

    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID
        orderMap.remove(orderId);
        if(orderToPartnerMap.containsKey(orderId)){// if pairing exists
            String partnerId=orderToPartnerMap.get(orderId);
            orderToPartnerMap.remove(orderId);// deleting order partner pair;
            HashSet<String> orderlist=partnerToOrderMap.get(partnerId);
            orderlist.remove(orderId);
            DeliveryPartner partner=findPartnerById(partnerId);
            partner.setNumberOfOrders(partner.getNumberOfOrders()-1);
        }

    }

    public Integer findCountOfUnassignedOrders(){
        // your code here
        return orderMap.size()-orderToPartnerMap.size();

    }

    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
        // your code here
        String []parts=timeString.split(":");
        int time=Integer.parseInt(parts[0])*60+Integer.parseInt(parts[1]);
        int count=0;
        if(partnerToOrderMap.containsKey(partnerId)){
            HashSet<String> orderlist=partnerToOrderMap.get(partnerId);
            for(String str:orderlist){
                Order order=orderMap.get(str);
                if(order.getDeliveryTime()>time){
                    count++;
                }
            }
        }

        return count;
    }

    public String findLastDeliveryTimeByPartnerId(String partnerId){
        // your code here
        // code should return string in format HH:MM
        int time=0;
        String LastDeliverytime="";
//        LocalDateTime currentDateTime=LocalDateTime.now();
        HashSet<String> orderlist=partnerToOrderMap.get(partnerId);
        for(String str:orderlist){
            Order order=orderMap.get(str);
            time=Math.max(time,order.getDeliveryTime());

//            int currentime=currentDateTime.getHour()*60+currentDateTime.getMinute();
//            if(time>currentime)//delivered
//LastDeliverytime=String.valueOf(time);

        }



        LastDeliverytime=String.valueOf(time/60)+":"+String.valueOf(time%60);

return LastDeliverytime;
    }
}