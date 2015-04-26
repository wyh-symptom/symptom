package test;

import java.io.Reader;
import java.util.Date;
import java.util.List;



import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;


public class Test {

	public static void main(String[] args) throws Exception {
//		String resource = "MapperConfig.xml";
//		Reader reader = Resources.getResourceAsReader(resource);
//		SqlSessionFactory sqlMapper = new SqlSessionFactoryBuilder()
//				.build(reader);
//		SqlSession sqlSession = sqlMapper.openSession();
//
//		MstAdministratorExample pet = new MstAdministratorExample();
//		try {
//			MstAdministratorMapper mapper = sqlSession.getMapper(MstAdministratorMapper.class);
//			MstAdministrator record = new MstAdministrator();
//			record.setLoginUser("user");
//			record.setLoginPass("123456");
//			record.setRoleCode(3);
//			record.setStatusCode(3);
//			record.setCreateDatetime(new Date());
//			mapper.insertSelective(record);	
//			sqlSession.commit();
////			List<MstAdministrator> allRecords = mapper.selectByExample(pet);
////			for (MstAdministrator s : allRecords)
//			System.out.println(record.getId());
//		} finally {
//			sqlSession.close();
//		}
	}
}
