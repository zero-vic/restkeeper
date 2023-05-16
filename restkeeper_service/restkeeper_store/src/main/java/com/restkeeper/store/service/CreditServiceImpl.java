package com.restkeeper.store.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.restkeeper.constant.SystemCode;
import com.restkeeper.exception.BussinessException;
import com.restkeeper.store.entity.Credit;
import com.restkeeper.store.entity.CreditCompanyUser;
import com.restkeeper.store.mapper.CreditMapper;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service("creditService")
@Service(version = "1.0.0",protocol = "dubbo")
public class CreditServiceImpl extends ServiceImpl<CreditMapper, Credit> implements ICreditService {


    @Autowired
    @Qualifier("creditCompanyUserService")
    private ICreditCompanyUserService creditCompanyUserService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean add(Credit credit, List<CreditCompanyUser> users) {
        this.save(credit);
        if (!CollectionUtils.isEmpty(users)){
            List<String> userNameList = users.stream().map(d -> d.getUserName()).collect(Collectors.toList());
            long count = userNameList.stream().distinct().count();
            if(userNameList.size()!=count){
                throw new RuntimeException("用户名重复");
            }
            // 设置关联
            users.forEach(d -> d.setCreditId(credit.getCreditId()));
            return creditCompanyUserService.saveBatch(users);
        }

        return true;
    }

    @Override
    public IPage<Credit> queryPage(int pageNum, int size, String username) {
        IPage<Credit> page = new Page<>(pageNum,size);
        QueryWrapper<Credit> queryWrapper = new QueryWrapper<>();
        //   StringEscapeUtils.escapeSql 主要是为了防止sql注入，例如典型的万能密码攻击’ or 1=1 ’
        if(!StringUtils.isEmpty(username)){
            queryWrapper.lambda().like(Credit::getUserName,username).or().inSql(Credit::getCreditId,
                    "select credit_id from t_credit_company_user where user_name like '%"+ StringEscapeUtils.escapeSql(username)+"%'");
        }
        page = this.page(page, queryWrapper);
        List<Credit> creditList = page.getRecords();
        //如果类型是公司，还需要设置相关的挂账人信息
        creditList.forEach(d->{
            if (d.getCreditType() == SystemCode.CREDIT_TYPE_COMPANY){

                QueryWrapper<CreditCompanyUser> queryWrapperCompanyUser = new QueryWrapper<>();

                queryWrapperCompanyUser.lambda().eq(CreditCompanyUser::getCreditId,d.getCreditId());

                d.setUsers(creditCompanyUserService.list(queryWrapperCompanyUser));
            }
        });

        return page;
    }

    @Override
    public Credit queryById(String id) {
        Credit credit = this.getById(id);
        if(credit == null){
            throw new RuntimeException("不存在挂账信息");
        }
        //企业挂账
        if(credit.getCreditType() == SystemCode.CREDIT_TYPE_COMPANY){
            QueryWrapper<CreditCompanyUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(CreditCompanyUser::getCreditId,credit.getCreditId());
            credit.setUsers(creditCompanyUserService.list(queryWrapper));
        }

        return credit;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateInfo(Credit credit, List<CreditCompanyUser> users) {

        //删除原有关系
        if (credit.getCreditType() == SystemCode.CREDIT_TYPE_COMPANY){
            List<CreditCompanyUser> userList = credit.getUsers();
            if (userList != null && !userList.isEmpty()){
                List<String> idList = userList.stream().map(d -> d.getId()).collect(Collectors.toList());
                creditCompanyUserService.removeByIds(idList);
            }
        }


        if(users != null&&!users.isEmpty()){

            //获取用户名列表
            List<String> userNameList= users.stream().map(d->d.getUserName()).collect(Collectors.toList());

            //去重判断
            long count = userNameList.stream().distinct().count();
            if(userNameList.size()!=count){
                throw new BussinessException("用户名重复");
            }
            //设置关联
            users.forEach(d->{
                d.setCreditId(credit.getCreditId());
            });
            return creditCompanyUserService.saveBatch(users);
        }
        return this.saveOrUpdate(credit);
    }
}
