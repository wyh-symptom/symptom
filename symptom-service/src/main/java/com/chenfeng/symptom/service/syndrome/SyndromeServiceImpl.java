package com.chenfeng.symptom.service.syndrome;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chenfeng.symptom.common.constant.Constant;
import com.chenfeng.symptom.domain.common.pagehelper.Page;
import com.chenfeng.symptom.domain.model.mybatis.Syndrome;
import com.chenfeng.symptom.domain.repository.mybatis.syndrome.SyndromeMapper;
import com.chenfeng.symptom.service.CrudServiceImpl;

@Service
@Transactional(readOnly = true)
public class SyndromeServiceImpl extends CrudServiceImpl<Syndrome, Long, SyndromeMapper> implements SyndromeService {
    @Resource
    @Override
    public void setRepository(SyndromeMapper syndromeMapper) {
        super.setRepository(syndromeMapper);
    }

    @Override
    @Transactional
    public void create(SyndromeCreateInput syndromeCreateInput) {
        Syndrome syndrome = new Syndrome();
        BeanUtils.copyProperties(syndromeCreateInput, syndrome);
        repository.insertSelective(syndrome);
    }

	@Override
	public List<Map<String, Object>> findSyndromeInitData() {
		List<SyndromeInitOutput> syndromeInitOutputs = new ArrayList<>();
		Map<String, List<Syndrome>> syndromeNameMap = new LinkedHashMap<>();
		List<Syndrome> syndromes  = repository.findAll();
		if (!syndromes.isEmpty()) {
			for (Syndrome syndrome : syndromes) {
				if (syndromeNameMap.containsKey(syndrome.getSymptomName())) {
					List<Syndrome> sdm = syndromeNameMap.get(syndrome.getSymptomName());
					sdm.add(syndrome);
					syndromeNameMap.put(syndrome.getSymptomName(), sdm);
				} else {
					List<Syndrome> sdm = new ArrayList<>();
					sdm.add(syndrome);
					syndromeNameMap.put(syndrome.getSymptomName(), sdm);
				}
			}
		}
		
		for (String syndromeName : syndromeNameMap.keySet()) {
			SyndromeInitOutput syndromeInitOutput = new SyndromeInitOutput();
			syndromeInitOutput.setSymptomName(syndromeName);
			syndromeInitOutput.setSymptomCategory(syndromeNameMap.get(syndromeName).get(0).getSymptomCategory());
			syndromeInitOutput.setSyndromes(syndromeNameMap.get(syndromeName));
			syndromeInitOutputs.add(syndromeInitOutput);
		}
		
		Map<String, List<SyndromeInitOutput>> syndromeInitOutputMap = new LinkedHashMap<>();
		for (SyndromeInitOutput syndromeInitOutput : syndromeInitOutputs) {
		    if (syndromeInitOutputMap.containsKey(syndromeInitOutput.getSymptomCategory())) {
		        List<SyndromeInitOutput> sdm = syndromeInitOutputMap.get(syndromeInitOutput.getSymptomCategory());
                sdm.add(syndromeInitOutput);
                syndromeInitOutputMap.put(syndromeInitOutput.getSymptomCategory(), sdm);
            } else {
                List<SyndromeInitOutput> sdm = new ArrayList<>();
                sdm.add(syndromeInitOutput);
                syndromeInitOutputMap.put(syndromeInitOutput.getSymptomCategory(), sdm);
            }
        }
		
		List<Map<String, Object>> maps = new ArrayList<>();
		for (String syndromeCategoryName : syndromeInitOutputMap.keySet()) {
		    Map<String, Object> syndromeInitOutputsMap = new LinkedHashMap<>();
            syndromeInitOutputsMap.put("syndromeCategoryName", syndromeCategoryName);
            syndromeInitOutputsMap.put("syndromeNames", syndromeInitOutputMap.get(syndromeCategoryName));
            maps.add(syndromeInitOutputsMap);
        }
		
		return maps;
	}

    @Override
    public List<Syndrome> findAll() {
        return repository.findAll();
    }

	@Override
	public List<Syndrome> findAllByZz(String zzName) {
		return repository.findAllByZz(zzName);
	}
	
	@Override
	@Transactional
	public Syndrome update(SyndromeCreateInput syndromeUpdateInput) {
	    Syndrome syndrome = new Syndrome();
        BeanUtils.copyProperties(syndromeUpdateInput, syndrome);
        this.update(syndrome);
        
        syndrome = repository.findNextSyndromeById(syndrome.getId());
        if (syndrome == null) {
            syndrome = repository.findFirstSyndrome();
        }
        
	    return syndrome;
	}

    @Override
    public Page<Syndrome> findPageSyndrome(int page, String syndromeName) {
        if (StringUtils.isBlank(syndromeName)) {
            syndromeName = null;
        }  else {
            syndromeName = syndromeName.trim();
        }
        
        return repository.findPageSyndrome(new RowBounds(page * Constant.PAGE_SIZE, Constant.PAGE_SIZE), syndromeName);
    }
}
