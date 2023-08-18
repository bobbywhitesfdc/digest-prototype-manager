/**
 * 
 */
package bobby.prototype.digest.servlet.bean;

import bobby.sfdc.prototype.json.FeedDirectoryItem;

/**
 * Extend the intrinsic FeedDirectoryItem and make it retain selection state and default status so that it can be sorted
 * 
 * @author bobby.white
 *
 */
public class FeedSelection extends FeedDirectoryItem implements Comparable<FeedSelection> {
	boolean defaultSelection=false;
	boolean selected=false;

	public FeedSelection(FeedDirectoryItem item) {
		super(item);
	}

	/**
	 * @return the defaultSelection
	 */
	public boolean isDefaultSelection() {
		return defaultSelection;
	}

	/**
	 * @param defaultSelection the defaultSelection to set
	 */
	public void setDefaultSelection(boolean defaultSelection) {
		this.defaultSelection = defaultSelection;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FeedSelection [defaultSelection=");
		builder.append(defaultSelection);
		builder.append(", selected=");
		builder.append(selected);
		builder.append(", ");
		if (feedElementsUrl != null) {
			builder.append("feedElementsUrl=");
			builder.append(feedElementsUrl);
			builder.append(", ");
		}
		if (feedItemsUrl != null) {
			builder.append("feedItemsUrl=");
			builder.append(feedItemsUrl);
			builder.append(", ");
		}
		if (feedType != null) {
			builder.append("feedType=");
			builder.append(feedType);
			builder.append(", ");
		}
		if (feedUrl != null) {
			builder.append("feedUrl=");
			builder.append(feedUrl);
			builder.append(", ");
		}
		if (keyPrefix != null) {
			builder.append("keyPrefix=");
			builder.append(keyPrefix);
			builder.append(", ");
		}
		if (label != null) {
			builder.append("label=");
			builder.append(label);
			builder.append(", ");
		}
		builder.append("isDefaultSelection()=");
		builder.append(isDefaultSelection());
		builder.append(", isSelected()=");
		builder.append(isSelected());
		builder.append(", ");
		if (getFeedElementsUrl() != null) {
			builder.append("getFeedElementsUrl()=");
			builder.append(getFeedElementsUrl());
			builder.append(", ");
		}
		if (getFeedItemsUrl() != null) {
			builder.append("getFeedItemsUrl()=");
			builder.append(getFeedItemsUrl());
			builder.append(", ");
		}
		if (getFeedType() != null) {
			builder.append("getFeedType()=");
			builder.append(getFeedType());
			builder.append(", ");
		}
		if (getFeedUrl() != null) {
			builder.append("getFeedUrl()=");
			builder.append(getFeedUrl());
			builder.append(", ");
		}
		if (getKeyPrefix() != null) {
			builder.append("getKeyPrefix()=");
			builder.append(getKeyPrefix());
			builder.append(", ");
		}
		if (getLabel() != null) {
			builder.append("getLabel()=");
			builder.append(getLabel());
			builder.append(", ");
		}
		if (super.toString() != null) {
			builder.append("toString()=");
			builder.append(super.toString());
			builder.append(", ");
		}
		if (getClass() != null) {
			builder.append("getClass()=");
			builder.append(getClass());
			builder.append(", ");
		}
		builder.append("hashCode()=");
		builder.append(hashCode());
		builder.append("]");
		return builder.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (defaultSelection ? 1231 : 1237);
		result = prime * result + (selected ? 1231 : 1237);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FeedSelection other = (FeedSelection) obj;
		if (defaultSelection != other.defaultSelection)
			return false;
		if (selected != other.selected)
			return false;
		return true;
	}

	/**
	 * Comparison is based on Selected > Default > Order by Label of Feed
	 * Requires that only 1 member of the list is selected and only one is default otherwise the order is unpredictable
	 */
	public int compareTo(FeedSelection o) {
		
		if (o == null) {
			return -1;
		}
		
		if (this.isDefaultSelection() || o.isDefaultSelection()) {
		  return new Boolean(!this.isDefaultSelection()).compareTo(new Boolean(!o.isDefaultSelection())); // flipped sense to get True < False
		}
		
		return this.getLabel().compareTo(o.getLabel());
	}

}
