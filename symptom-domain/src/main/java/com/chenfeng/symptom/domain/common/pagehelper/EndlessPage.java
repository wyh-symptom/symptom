package com.chenfeng.symptom.domain.common.pagehelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = EndlessPageSerializer.class) 
public class EndlessPage<E> implements Page<E> {

    private boolean lastPage;
    
    private List<E> content = new ArrayList<E>();
    
    
	public boolean isLastPage() {
		return lastPage;
	}

	public void setLastPage(boolean lastPage) {
		this.lastPage = lastPage;
	}

	public List<E> getContent() {
		return content;
	}

	public int size() {
		return content.size();
	}

	public boolean isEmpty() {
		return content.isEmpty();
	}

	public boolean contains(Object o) {
		return content.contains(o);
	}

	public Iterator<E> iterator() {
		return content.iterator();
	}

	public Object[] toArray() {
		return content.toArray();
	}

	public <T> T[] toArray(T[] a) {
		return content.toArray(a);
	}

	public boolean add(E e) {
		return content.add(e);
	}

	public boolean remove(Object o) {
		return content.remove(o);
	}

	public boolean containsAll(Collection<?> c) {
		return content.containsAll(c);
	}

	public boolean addAll(Collection<? extends E> c) {
		return content.addAll(c);
	}

	public boolean addAll(int index, Collection<? extends E> c) {
		return content.addAll(index, c);
	}

	public boolean removeAll(Collection<?> c) {
		return content.removeAll(c);
	}

	public boolean retainAll(Collection<?> c) {
		return content.retainAll(c);
	}

	public void clear() {
		content.clear();
	}


	public E get(int index) {
		return content.get(index);
	}

	public E set(int index, E element) {
		return content.set(index, element);
	}

	public void add(int index, E element) {
		content.add(index, element);
	}

	public E remove(int index) {
		return content.remove(index);
	}

	public int indexOf(Object o) {
		return content.indexOf(o);
	}

	public int lastIndexOf(Object o) {
		return content.lastIndexOf(o);
	}

	public ListIterator<E> listIterator() {
		return content.listIterator();
	}

	public ListIterator<E> listIterator(int index) {
		return content.listIterator(index);
	}

	public List<E> subList(int fromIndex, int toIndex) {
		return content.subList(fromIndex, toIndex);
	}

}
